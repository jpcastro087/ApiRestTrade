package com.api.trade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class Piso {
    private int nroPiso;
    private double porcentajeBajada;
    private String precioEntrada;
    private double porcentajeTakeProfit;

    private double amount;

    private double porcentajeInvertido;

    public void setNroPiso(int nroPiso) {
        this.nroPiso = nroPiso;
    }

    public void setPorcentajeBajada(double porcentajeBajada) {
        this.porcentajeBajada = porcentajeBajada;
    }

    public void setPrecioEntrada(String precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public int getNroPiso() {
        return nroPiso;
    }

    public double getPorcentajeBajada() {
        return porcentajeBajada;
    }

    public String getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPorcentajeTakeProfit(double porcentajeTakeProfit) {
        this.porcentajeTakeProfit = porcentajeTakeProfit;
    }

    public double getPorcentajeTakeProfit() {
        return porcentajeTakeProfit;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setPorcentajeInvertido(double porcentajeInvertido) {
        this.porcentajeInvertido = porcentajeInvertido;
    }

    public double getPorcentajeInvertido() {
        return porcentajeInvertido;
    }
}

public class Main {

    public static List<Piso> calcularPisos(BigDecimal precioDesde,
                                           BigDecimal precioHasta,
                                           int cantidadPisos,
                                           BigDecimal  cantidadTotalParaRepartirEntrePisos,
                                           BigDecimal precioActual,
                                           BigDecimal precioActualMonedaFiat) {
        List<Piso> pisos = new ArrayList<>();

        BigDecimal porcentajeBajadaPrecioDesdeHasta = precioHasta.subtract(precioDesde).divide(precioHasta, 5, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
        BigDecimal porcentajeSubidaPrecioDesdeHasta = precioHasta.subtract(precioDesde).divide(precioDesde, 5, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));

        BigDecimal porcentajeBajadaPorCadaPiso = porcentajeBajadaPrecioDesdeHasta.divide(BigDecimal.valueOf(cantidadPisos), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal porcentajeTakeProfitPorCadaPiso = porcentajeSubidaPrecioDesdeHasta.divide(BigDecimal.valueOf(cantidadPisos), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal amountPorPiso = cantidadTotalParaRepartirEntrePisos.divide(BigDecimal.valueOf(cantidadPisos), 5, BigDecimal.ROUND_HALF_UP);
        BigDecimal porcentajeInvertidoPorPiso = amountPorPiso.divide(cantidadTotalParaRepartirEntrePisos, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));

        BigDecimal precioEntrada = precioHasta;

        for (int i = 1; i <= cantidadPisos; i++) {
            BigDecimal porcentaje = (i == 1) ? BigDecimal.ZERO : porcentajeBajadaPorCadaPiso.negate();
            BigDecimal bajada = precioEntrada.multiply(porcentaje.abs().divide(BigDecimal.valueOf(100), 15, BigDecimal.ROUND_HALF_UP));
            precioEntrada = precioEntrada.subtract(bajada);

            BigDecimal amount = new BigDecimal("0");
            if( precioActualMonedaFiat.equals(new BigDecimal("1"))){
                amount = amountPorPiso.divide(precioActual, RoundingMode.HALF_UP);
            } else {
                amount = amountPorPiso.multiply(precioActualMonedaFiat).divide(precioActual, RoundingMode.HALF_UP);
            }



            Piso piso = new Piso();
            piso.setNroPiso(i);
            piso.setPorcentajeBajada(porcentaje.doubleValue());
            piso.setPrecioEntrada(precioEntrada.toPlainString());
            piso.setPorcentajeTakeProfit(porcentajeTakeProfitPorCadaPiso.doubleValue());
            piso.setAmount(amount.doubleValue());
            piso.setPorcentajeInvertido(porcentajeInvertidoPorPiso.doubleValue());
            pisos.add(piso);
        }

        return pisos;
    }

    public static void main(String[] args) {
        BigDecimal precioDesde = new BigDecimal("0.20");
        BigDecimal precioHasta = new BigDecimal("0.56");

        BigDecimal precioActual = new BigDecimal("0.53");
        BigDecimal precioActualMonedaFiat = new BigDecimal("1");
        BigDecimal cantidadTotalParaRepartirEntrePisos = new BigDecimal("200");
        int cantidadPisos = 10;

        List<Piso> listaPisos = calcularPisos(precioDesde,
                precioHasta,
                cantidadPisos,
                cantidadTotalParaRepartirEntrePisos,
                precioActual,
                precioActualMonedaFiat);


        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00000000000000000000000");

        for (Piso piso : listaPisos) {
            System.out.println("Piso " + piso.getNroPiso() + " = porcentajeBajada " + piso.getPorcentajeBajada() +
                    " precioEntrada " + piso.getPrecioEntrada() + " takeProfit " + piso.getPorcentajeTakeProfit() + " amount " + decimalFormat.format(piso.getAmount()) + " porcentajeInvertidoPiso " + piso.getPorcentajeInvertido() );
        }
    }
}
