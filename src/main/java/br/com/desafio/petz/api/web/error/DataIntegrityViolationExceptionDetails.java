package br.com.desafio.petz.api.web.error;

import java.util.Date;

public class DataIntegrityViolationExceptionDetails extends ErrorDetail {
	
	private DataIntegrityViolationExceptionDetails(Builder builder) {
		this.titulo = builder.titulo;
		this.detalhe = builder.detalhe;
		this.statusCode = builder.statusCode;
		this.timestamp = builder.timestamp;
		this.developerMessage = builder.developerMessage;
	}
	
	public DataIntegrityViolationExceptionDetails() {
	}

	public String getTitulo() {
		return titulo;
	}
	public String getDetalhe() {
		return detalhe;
	}
	public int getStatusCode() {
		return statusCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public Date getTimestamp() {
		return timestamp;
	}


	public static Builder builder() {
		return new Builder();
	}

	
	public static final class Builder {
		private String titulo;
		private String detalhe;
		private int statusCode;
		private Date timestamp;
		private String developerMessage;

		private Builder() {
		}

		public Builder titulo(String titulo) {
			this.titulo = titulo;
			return this;
		}

		public Builder detalhe(String detalhe) {
			this.detalhe = detalhe;
			return this;
		}

		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder timestamp(Date timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder developerMessage(String developerMessage) {
			this.developerMessage = developerMessage;
			return this;
		}

		public DataIntegrityViolationExceptionDetails build() {
			return new DataIntegrityViolationExceptionDetails(this);
		}
	}
}
