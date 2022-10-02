package com.demo1.rates1.dao;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CurrencyCodesRepository extends JpaRepository<CurrencyCodes, Long> 
{
	/**
	 * Check if CurrencyCodes already available for provided input string
	 * @param inputStr
	 * @param expiryTime
	 * @return
	 */
	Optional<CurrencyCodes> findByInputStrAndExpiryTimeGreaterThan(
			String inputStr,LocalDateTime expiryTime);
	
	/**
	 * Delete Existing Currency rows if present
	 */
	@Modifying
	@Query("delete from CurrencyCodes c where c.inputStr = :inputStr1")	
	int delete_existing_currency_rows(@Param("inputStr1") String inputStr);		
}
