package com.demo1.rates1.dao;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "curncy_codes_info",
indexes = 
 {   @Index(columnList = "create_time", name = "zipwthr_create_time_aidx"), //createTime
     @Index(columnList = "expiry_time", name = "zipwthr_expiry_time_aidx") //expiryTime
 
})
public class CurrencyCodes {

	@Id
	@Column(unique = true, nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Column(name = "create_time")
	private LocalDateTime createTime = null;
	
	@Column(name = "expiry_time")
	@NotNull
	private LocalDateTime expiryTime;
	
    @Column(name = "input_str", unique = true)
	@NotEmpty	
	@Length(min = 2, max=20)
	private String inputStr = null;
    
    @Column(name = "response_str")
	@NotEmpty
	@Length(min = 2, max=2000)
	private String responseStr = null;
    
	public CurrencyCodes() {
		super();		
	}

	public CurrencyCodes(@NotNull LocalDateTime createTime, @NotNull LocalDateTime expiryTime,
			@NotEmpty @Length(min = 2, max = 20) String inputStr,
			@NotEmpty @Length(min = 2, max = 20) String responseStr) {
		super();
		this.createTime = createTime;
		this.expiryTime = expiryTime;
		this.inputStr = inputStr;
		this.responseStr = responseStr;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(LocalDateTime expiryTime) {
		this.expiryTime = expiryTime;
	}

	public String getInputStr() {
		return inputStr;
	}

	public void setInputStr(String inputStr) {
		this.inputStr = inputStr;
	}

	public String getResponseStr() {
		return responseStr;
	}

	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrencyCodes other = (CurrencyCodes) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurrencyCodes [id=").append(id).append(", createTime=").append(createTime)
				.append(", expiryTime=").append(expiryTime).append(", inputStr=").append(inputStr)
				.append(", responseStr=").append(responseStr).append("]");
		return builder.toString();
	}


}
