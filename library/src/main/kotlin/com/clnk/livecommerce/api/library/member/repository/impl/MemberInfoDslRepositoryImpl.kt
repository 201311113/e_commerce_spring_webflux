package com.clnk.livecommerce.api.library.member.repository.impl

import com.clnk.livecommerce.api.library.member.MemberInfo
import com.clnk.livecommerce.api.library.member.MemberSearchCondition
import com.clnk.livecommerce.api.library.member.QMemberInfo.memberInfo
import com.clnk.livecommerce.api.library.member.repository.MemberInfoDslRepository
import com.clnk.livecommerce.api.library.support.QuerydslCustomRepositorySupport
import com.querydsl.core.BooleanBuilder
import mu.KotlinLogging
import org.apache.commons.lang3.EnumUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.util.MultiValueMap

private val log = KotlinLogging.logger {}

@Repository
class MemberInfoRepositoryImpl : QuerydslCustomRepositorySupport(MemberInfo::class.java), MemberInfoDslRepository {
    override fun findAllBySearch(pageable: Pageable, queryParams: MultiValueMap<String, String>): Page<MemberInfo> {
        log.debug { "]-----] MemberInfoRepositoryImpl::findAllBySearch.queryParams [-----[ ${queryParams} " }
        val whereClause = BooleanBuilder()
        if (queryParams.size > 0) {
            for (key in queryParams.keys) {
                if (EnumUtils.isValidEnum(MemberSearchCondition::class.java, key.toUpperCase())) {
                    val searchContent = queryParams[key]!![0]
                    if (StringUtils.isNoneBlank(searchContent)) {
                        when (key.toLowerCase()) {
                            MemberSearchCondition.NICKNAME.searchKey -> whereClause.apply { this.and(memberInfo.nickName.like("%${searchContent}%")) }
                            MemberSearchCondition.PHONENUMBER.searchKey -> whereClause.apply { this.and(memberInfo.phoneNumber.like("%${searchContent}%")) }
                        }
                    }
                }
            }
        }
        val query = select(memberInfo).from(memberInfo).where(memberInfo.active.eq(true).and(whereClause))
        val members: List<MemberInfo> = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(members, pageable, query.fetchCount())
    }

}
