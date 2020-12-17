package com.clnk.livecommerce.api.broadcast.service.impl

import com.clnk.livecommerce.api.broadcast.*
import com.clnk.livecommerce.api.broadcast.repository.BroadcastOnSaleItemRepository
import com.clnk.livecommerce.api.broadcast.repository.BroadcastRepository
import com.clnk.livecommerce.api.broadcast.service.BroadcastService
import com.clnk.livecommerce.api.infra.MediaUtils
import com.clnk.livecommerce.api.media.Media
import com.clnk.livecommerce.api.media.repository.MediaRepository
import com.clnk.livecommerce.api.onsaleitem.repository.OnSaleItemRepository
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.MultiValueMap
import javax.persistence.EntityNotFoundException

private val log = KotlinLogging.logger {}

@Service
class BroadcastServiceImpl(
    private var broadcastRepository: BroadcastRepository,
    private var broadcastOnSaleItemRepository: BroadcastOnSaleItemRepository,
    private var onSaleItemRepository: OnSaleItemRepository,
    private var mediaRepository: MediaRepository,
    private var modelMapper: ModelMapper,
    private var mediaUtils: MediaUtils,
) : BroadcastService {
    @Transactional
    override fun create(req: BroadcastReq, adminId: Long): CreateBroadcastRes {
        log.info { "]-----] BroadcastServiceImpl::create CreateBroadcastReq [-----[ ${req}" }
        val newBroadcast = Broadcast(
            title = req.title,
            description = req.description,
            startAt = req.startAt,
            endAt = req.endAt,
        )
        broadcastRepository.save(newBroadcast)
        if (req.onSaleItemIds.size > 0) {
            val onSaleItems: MutableList<BroadcastOnSaleItem> = mutableListOf()
            for (k in req.onSaleItemIds.indices) {
                val onSaleItem = onSaleItemRepository.findByIdAndActive(req.onSaleItemIds[k], true)
                    ?: throw EntityNotFoundException("not found a OnSaleItem(id = ${req.onSaleItemIds[k]})")
                val newBroadcastOnSaleItem = BroadcastOnSaleItem(newBroadcast, onSaleItem)
                onSaleItems.add(newBroadcastOnSaleItem)
            }
            broadcastOnSaleItemRepository.saveAll(onSaleItems)
        }
        if (req.newImages.size > 0) {
            val medias: MutableList<Media> = mutableListOf()
            for (i in req.newImages.indices) {
                val mediaInfo = req.newImages[i].newImage?.let { mediaUtils.getMediaInfo(it, "broadcast") }
                val newMedia = Media(
                    mediaUuid = newBroadcast.mediaUuid,
                    url = mediaInfo!!.fullPath,
                    originName = mediaInfo.originalName,
                    modifyName = mediaInfo.modifyName,
                    pathS3 = mediaInfo.bucketPath,
                    imageExt = mediaInfo.imageExt,
                    sortPosition = req.newImages[i].sortPosition
                )
                medias.add(newMedia)
            }
            mediaRepository.saveAll(medias)
        }
        return CreateBroadcastRes(newBroadcast.id!!)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<BroadcastRes> {
        val broadcasts = broadcastRepository.findAllBySearch(pageable, queryParams)
        return broadcasts.map {
            modelMapper.map(it, BroadcastRes::class.java)
        }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): BroadcastRes {
        val item = broadcastRepository.findByIdAndActive(id, true)
        return modelMapper.map(item, BroadcastRes::class.java)
    }

    @Transactional
    override fun update(id: Long, req: BroadcastReq, adminId: Long): CreateBroadcastRes {
        log.info { "]-----] BroadcastServiceImpl::update CreateBroadcastReq[-----[ ${req}" }
        val broadcast = broadcastRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a Broadcast(id = ${id})")
        broadcast.title = req.title
        broadcast.description = req.description
        broadcast.startAt = req.startAt
        broadcast.endAt = req.endAt
        broadcastRepository.save(broadcast)
        if (req.deletedOnSaleItemIds.size > 0) {
            broadcastOnSaleItemRepository.deleteByIds(req.deletedOnSaleItemIds, broadcast.id!!)
        }
        if (req.onSaleItemIds.size > 0) {
            val onSaleItems: MutableList<BroadcastOnSaleItem> = mutableListOf()
            for (k in req.onSaleItemIds.indices) {
                val onSaleItem = onSaleItemRepository.findByIdAndActive(req.onSaleItemIds[k], true)
                    ?: throw EntityNotFoundException("not found a OnSaleItem(id = ${req.onSaleItemIds[k]})")
                val newBroadcastOnSaleItem = BroadcastOnSaleItem(broadcast, onSaleItem)
                onSaleItems.add(newBroadcastOnSaleItem)
            }
            broadcastOnSaleItemRepository.saveAll(onSaleItems)
        }

        if (req.newImages.size > 0) {
            val medias: MutableList<Media> = mutableListOf()
            for (i in req.newImages.indices) {
                val mediaInfo = req.newImages[i].newImage?.let { mediaUtils.getMediaInfo(it, "broadcast") }
                val newMedia = Media(
                    mediaUuid = broadcast.mediaUuid,
                    url = mediaInfo!!.fullPath,
                    originName = mediaInfo.originalName,
                    modifyName = mediaInfo.modifyName,
                    pathS3 = mediaInfo.bucketPath,
                    imageExt = mediaInfo.imageExt,
                    sortPosition = req.newImages[i].sortPosition
                )
                medias.add(newMedia)
            }
            mediaRepository.saveAll(medias)
        }

        if (req.updatedImages.size > 0) {
            for (j in req.updatedImages.indices) {
                val media = mediaRepository.findByIdAndMediaUuidAndActive(req.updatedImages[j].id, broadcast.mediaUuid, true)
                    ?: throw EntityNotFoundException("not found a Media(id = ${req.updatedImages[j].id})")
                media.sortPosition = req.updatedImages[j].sortPosition
                mediaRepository.save(media)
            }
        }
        if (req.deletedImages.size > 0) {
            mediaRepository.deleteByIds(req.deletedImages, broadcast.mediaUuid)
        }
        return CreateBroadcastRes(broadcast.id!!)
    }
}