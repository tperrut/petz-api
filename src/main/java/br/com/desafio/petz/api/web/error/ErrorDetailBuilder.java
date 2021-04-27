package br.com.desafio.petz.api.web.error;

import java.util.Date;

public class ErrorDetailBuilder {
    protected String titulo;
    protected String detalhe;
    protected int statusCode;
    protected Date timestamp;
    protected String developerMessage;

    public static ErrorDetailBuilder withBuilder() {
        return new ErrorDetailBuilder();
    }

    public ErrorDetailBuilder withTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public ErrorDetailBuilder withDetalhe(String detalhe) {
        this.detalhe = detalhe;
        return this;
    }

    public ErrorDetailBuilder withStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ErrorDetailBuilder withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ErrorDetailBuilder withDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
        return this;
    }

    public ErrorDetail build() {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTitulo(titulo);
        errorDetail.setDetalhe(detalhe);
        errorDetail.setStatusCode(statusCode);
        errorDetail.setTimestamp(timestamp);
        errorDetail.setDeveloperMessage(developerMessage);
        return errorDetail;
    }
}
