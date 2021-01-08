package com.clnk.livecommerce.api.library.preview.service.impl

import com.clnk.livecommerce.api.library.infra.MediaUtils
import com.clnk.livecommerce.api.library.media.Media
import com.clnk.livecommerce.api.library.media.repository.MediaRepository
import com.clnk.livecommerce.api.library.preview.CreatePreviewReq
import com.clnk.livecommerce.api.library.preview.CreatePreviewRes
import com.clnk.livecommerce.api.library.preview.Preview
import com.clnk.livecommerce.api.library.preview.PreviewRes
import com.clnk.livecommerce.api.library.preview.repository.PreviewRepository
import com.clnk.livecommerce.api.library.preview.service.PreviewService
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
class PreviewServiceImpl(

    private var previewRepository: PreviewRepository,
    private var mediaRepository: MediaRepository,
    private var modelMapper: ModelMapper,
    private var mediaUtils: MediaUtils
) : PreviewService {
    @Transactional
    override fun create(req: CreatePreviewReq, adminId: Long): CreatePreviewRes {
        log.info { "]-----] PreviewServiceImpl::create CreatePreviewReq[-----[ ${req}" }

        val newPreview = Preview(
            title = req.title,
            description = req.description,
            startAt = req.startAt,
            endAt = req.endAt,
            sortPosition = req.sortPosition
        )
        previewRepository.save(newPreview)
        if (req.newImages.size > 0) {
            val medias: MutableList<Media> = mutableListOf()
            for (i in req.newImages.indices) {
                val mediaInfo = req.newImages[i].newImage?.let { mediaUtils.getMediaInfo(it, "preview") }
                val newMedia = Media(
                    mediaUuid = newPreview.mediaUuid,
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

        return CreatePreviewRes(newPreview.id!!)
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<PreviewRes> {
        val previews = previewRepository.findAllBySearch(pageable, queryParams)
        return previews.map {
            modelMapper.map(it, PreviewRes::class.java)
        }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): PreviewRes {
        val item = previewRepository.findByIdAndActive(id, true)
        return modelMapper.map(item, PreviewRes::class.java)
    }

    @Transactional
    override fun update(id: Long, req: CreatePreviewReq, adminId: Long): CreatePreviewRes {
        log.info { "]-----] PreviewServiceImpl::update CreatePreviewReq[-----[ ${req}" }
        val preview = previewRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a Preview(id = ${id})")
        preview.title = req.title
        preview.description = req.description
        preview.startAt = req.startAt
        preview.endAt = req.endAt
        preview.sortPosition = req.sortPosition
        previewRepository.save(preview)

        if (req.newImages.size > 0) {
            val medias: MutableList<Media> = mutableListOf()
            for (i in req.newImages.indices) {
                val mediaInfo = req.newImages[i].newImage?.let { mediaUtils.getMediaInfo(it, "preview") }
                val newMedia = Media(
                    mediaUuid = preview.mediaUuid,
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
        log.info { "]-----] PreviewServiceImpl::update updatedImages[-----[ ${req.updatedImages.size}" }
        log.info { "]-----] PreviewServiceImpl::update deletedImages[-----[ ${req.deletedImages.size}" }
        if (req.updatedImages.size > 0) {
            for (j in req.updatedImages.indices) {
                val media = mediaRepository.findByIdAndMediaUuidAndActive(req.updatedImages[j].id, preview.mediaUuid, true)
                    ?: throw EntityNotFoundException("not found a Media(id = ${req.updatedImages[j].id})")
                media.sortPosition = req.updatedImages[j].sortPosition
                mediaRepository.save(media)
            }
        }
        if (req.deletedImages.size > 0) {
            mediaRepository.deleteByIds(req.deletedImages, preview.mediaUuid)
        }
        return CreatePreviewRes(preview.id!!)
    }
}