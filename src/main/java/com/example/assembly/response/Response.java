package com.example.assembly.response;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

public class Response implements Serializable{
	
	private String status;

	private Integer result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", result=" + result + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(result, status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Response other = (Response) obj;
		return Objects.equals(result, other.result) && Objects.equals(status, other.status);
	}

}