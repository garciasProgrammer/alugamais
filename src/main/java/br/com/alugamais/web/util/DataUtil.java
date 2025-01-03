/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.alugamais.web.util;

import javax.swing.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author kleyson Santiago
 */
public class DataUtil {

    public String formataData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
 
        return dateFormat.format(new Date());
    }

    public String formataData(Date data) {
    	
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(data);
    }

    public Calendar formataDataStringToCalendar(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();       
        try {
            cal.setTime(dateFormat.parse(data));
            return cal;
        } catch (Exception ex) {
        	
        	return null;
            
        }
       
    }

    public Calendar formataDataDoisDigitos(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(data));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível converter a data de Início de Contrato.\nMensagem de Erro: " + ex.getMessage(), "Erro - Cadastro de Contrato", 2);
        }
        return cal;
    }

    public Calendar calcularDataDeVencimento(Calendar data, int prazo) {
        Calendar d = data;
        d.add(Calendar.MONTH, prazo);
        String formatada = formataData(d.getTime());
        return formataDataStringToCalendar(formatada);
    }

    public Calendar calcularDataDeVencimentoDiaDoMes(Calendar data, int prazo) {
        Calendar d = data;
        d.add(Calendar.MONTH, prazo);
        String formatada = formataData(d.getTime());
        return formataDataStringToCalendar(formatada);
    }

    public Calendar calcularDataDeVencimentoAluguel(Calendar data, int prazo) {
        Calendar d = data;
        d.add(Calendar.DAY_OF_WEEK, prazo);
        String formatada = formataData(d.getTime());
        return formataDataStringToCalendar(formatada);
    }

    public int obtemQuantidadeDeDiasEntreDatas(String dataInicio, String dataFinal) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicial = LocalDate.parse(dataInicio, format);
        LocalDate dataFim = LocalDate.parse(dataFinal, format);
        int a = (int) ChronoUnit.DAYS.between(dataInicial, dataFim);

        return (int) ChronoUnit.DAYS.between(dataInicial, dataFim);
    }

    public int obtemQuantidadeDeMesesEntreDatas(String dataInicio, String dataFinal) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicial = LocalDate.parse(dataInicio, format);
        LocalDate dataFim = LocalDate.parse(dataFinal, format);
        int a = (int) ChronoUnit.MONTHS.between(dataInicial, dataFim);

        return a;
    }


    public String obtemDataSeguinte(String dataInicio) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataInicial = LocalDate.parse(dataInicio, format);

        return dataInicial.plusDays(1).format(format);
    }

    public LocalDate convertStringForLcalDate(String data) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataConvertida = LocalDate.parse(data, format);

        return dataConvertida;
    }

    public String weekDay(Calendar cal) {
        return new DateFormatSymbols().getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)];
    }

}
