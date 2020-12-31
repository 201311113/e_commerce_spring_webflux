package com.clnk.livecommerce.api.library.member.repository

import com.clnk.livecommerce.api.library.member.MemberCert
import org.springframework.data.jpa.repository.JpaRepository

interface MemberCertRepository : JpaRepository<MemberCert, Long> {
    fun findByIdAndActive(id: Long, active: Boolean): MemberCert?
    fun findByCertPhoneNumberAndCertNumberAndActive(certPhoneNumber: String, certNumber: String, active: Boolean): MemberCert?
    fun findFirstByCertPhoneNumberAndActiveOrderByIdDesc(certPhoneNumber: String, active: Boolean): MemberCert?
}