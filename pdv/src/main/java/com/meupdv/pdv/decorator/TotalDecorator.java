package com.meupdv.pdv.decorator;

public abstract class TotalDecorator implements CalculoTotal {

    protected final CalculoTotal origem;

    protected TotalDecorator(CalculoTotal origem) {
        this.origem = origem;
    }
}