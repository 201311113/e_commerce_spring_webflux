package com.cucurbita.api.mentoitem.service.impl

import com.cucurbita.api.category.MentoItemCategory
import com.cucurbita.api.category.repository.CategoryRepository
import com.cucurbita.api.category.repository.MentoItemCategoryRepository
import com.cucurbita.api.infra.MediaUtils
import com.cucurbita.api.media.Media
import com.cucurbita.api.media.repository.MediaRepository
import com.cucurbita.api.mento.repository.MentoRepository
import com.cucurbita.api.mentoitem.*
import com.cucurbita.api.mentoitem.repository.MentoItemRepository
import com.cucurbita.api.mentoitem.service.MentoItemService
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import javax.persistence.EntityNotFoundException

private val log = KotlinLogging.logger {}

@Service
class MentoItemServiceImpl(
    private val mentoRepository: MentoRepository,
    private val mentoItemRepository: MentoItemRepository,
    private val mediaRepository: MediaRepository,
    private val mentoItemCategoryRepository: MentoItemCategoryRepository,
    private val categoryRepository: CategoryRepository,
    private var modelMapper: ModelMapper,
    private var mediaUtils: MediaUtils,
) : MentoItemService {
    @Transactional
    override fun create(req: CreateMentoItemReq, memberId: Long): CreateMentoItemRes {
        log.info { "]-----] MentoServiceImpl::create CreateMentoReq[-----[ ${req}" }
        val mento = mentoRepository.findByMemberIdAndActive(memberId, true)
            ?: throw EntityNotFoundException("not found a Mento(id = ${memberId})")

        var newMentoItem = MentoItem(
            title = req.title,
            status = ItemStatus.INIT,
            sellPrice = BigDecimal("65000"),
            stock = req.stock,
            description = req.description,
            startedAt = req.startedAt,
            endedAt = req.endedAt,
            hashTags = req.hashTags,
            mento = mento
        )
        mentoItemRepository.save(newMentoItem)
        if (req.categorySub != null) {
            val categorySub = categoryRepository.findByIdAndActive(req.categorySub!!, true)
                ?: throw EntityNotFoundException("not found a Category(id = ${req.categorySub})")
            mentoItemCategoryRepository.save(MentoItemCategory(
                newMentoItem, categorySub
            ))
        }
        if (req.categoryRoot != null) {
            val categoryRoot = categoryRepository.findByIdAndActive(req.categoryRoot!!, true)
                ?: throw EntityNotFoundException("not found a Category(id = ${req.categoryRoot})")
            mentoItemCategoryRepository.save(MentoItemCategory(
                newMentoItem, categoryRoot
            ))
        }
        val mediaInfo = req.itemImage?.let { mediaUtils.getMediaInfo(it, "mentoitem") }
        var newMedia = Media(
            mediaUuid = newMentoItem.mediaUuid,
            url = mediaInfo!!.fullPath,
            originName = mediaInfo!!.originalName,
            modifyName = mediaInfo!!.modifyName,
            pathS3 = mediaInfo!!.bucketPath,
            imageExt = mediaInfo!!.imageExt,
        )
        mediaRepository.save(newMedia)
        return CreateMentoItemRes(-1)
    }


    @Transactional(readOnly = true)
    override fun findAllByMemberId(pageable: Pageable, memberId: Long): Page<MentoItemResForList> {
        val mento = mentoRepository.findByMemberIdAndActive(memberId, true)
            ?: throw EntityNotFoundException("not found a Mento(memberId = ${memberId})")
        val items = mentoItemRepository.findAllByMentoIdAndActive(pageable, mento.id!!, true)
        return items.map {
            modelMapper.map(it, MentoItemResForList::class.java)
        }
    }

    @Transactional(readOnly = true)
    override fun findAllByStatus(pageable: Pageable): Page<MentoItemResForList> {
        val items = mentoItemRepository.findAllByActiveAndStatus(pageable, true, ItemStatus.LISTED)
        return items.map {
            modelMapper.map(it, MentoItemResForList::class.java)
        }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): MentoItemRes {
        val item = mentoItemRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a MentoItem(id = ${id})")
        return modelMapper.map(item, MentoItemRes::class.java)
    }

    @Transactional
    override fun updateStatus(adminId: Long, id: Long): MentoItemStatusRes {
        val item = mentoItemRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a MentoItem(id = ${id})")
        item.status = ItemStatus.LISTED
        mentoItemRepository.save(item)
        return MentoItemStatusRes(
            id = item.id!!,
            status = item.status
        )
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<MentoItemResForList> {
        val items = mentoItemRepository.findAllByActive(pageable, true)
        return items.map {
            modelMapper.map(it, MentoItemResForList::class.java)
        }
    }

}