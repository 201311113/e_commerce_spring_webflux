package com.clnk.livecommerce.api.library.sample.service.impl

import com.clnk.livecommerce.api.library.sample.CreateSampleReq
import com.clnk.livecommerce.api.library.sample.CreateSampleRes
import com.clnk.livecommerce.api.library.sample.Sample
import com.clnk.livecommerce.api.library.sample.SampleRes
import com.clnk.livecommerce.api.library.sample.repository.SampleRepository
import com.clnk.livecommerce.api.library.sample.service.SampleService
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class SampleServiceImpl(
    private val sampleRepository: SampleRepository,
    private val mapper: ModelMapper
) : SampleService {
    @Transactional(readOnly = true)
    override fun allSamples(pageable: Pageable): Page<SampleRes> {
        val sort = Sort.by(Sort.Direction.DESC, "id")
        val samples = sampleRepository.findAllByPage(pageable)
        return samples.map { mapper.map(it, SampleRes::class.java) }
    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): SampleRes {
        val sample = sampleRepository.findByIdAndActive(id, true)
            ?: throw EntityNotFoundException("not found a Sample(id = ${id})")
        return mapper.map(sample, SampleRes::class.java)
    }

    @Transactional
    override fun create(req: CreateSampleReq): CreateSampleRes {
        var newSample = mapper.map(req, Sample::class.java)
        sampleRepository.save(newSample)
        return CreateSampleRes(newSample.id!!)
    }
}