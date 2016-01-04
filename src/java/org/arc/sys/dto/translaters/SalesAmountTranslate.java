/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.arc.sys.dto.translaters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.arc.sys.dto.SalesAmountDto;
import org.arc.sys.hibernate.entities.SalesAmount;

/**
 *
 * @author angel
 */
public class SalesAmountTranslate {

    public static SalesAmountDto salesAmountTosalesAmountDto(SalesAmount sa){
        SalesAmountDto sadto = new SalesAmountDto();
        
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
    
        sadto.setActive(sa.isActive());
        sadto.setAmount(sa.getAmount());
        sadto.setBranchId(sa.getBranchId());
        if(sa.getDateEnd() != null)
            sadto.setDateEnd(format.format(sa.getDateEnd()));
        if(sa.getDateStart()!= null)
            sadto.setDateStart(format.format(sa.getDateStart()));
        sadto.setId(sa.getId());
        sadto.setUserId(sa.getUserId());
        
        return sadto;
    }
    
    public static SalesAmount salesAmountDtoToSalesAmount(SalesAmountDto saDto){
        SalesAmount sa = new SalesAmount();
        
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
    
        sa.setActive(saDto.isActive());
        sa.setAmount(saDto.getAmount());
        sa.setBranchId(saDto.getBranchId());
        sa.setId(saDto.getId());
        sa.setUserId(saDto.getUserId());
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String time = dateFormat.format(date);
            sa.setDateEnd(format.parse(saDto.getDateEnd()+" "+time.split(" ")[1]));
            System.out.println("1.- bbbbbbbbbbbbbbbbbbbbbbbb:: "+saDto.getDateEnd()+" || "+sa.getDateEnd());
        }catch(ParseException pe){
            sa.setDateEnd(null);
            System.out.println("Error de Date: "+pe.getMessage());
        }
        try{
            sa.setDateStart(format.parse(saDto.getDateStart()));
            System.out.println("2. bbbbbbbbbbbbbbbbbbbbbbbb:: "+saDto.getDateStart()+" || "+sa.getDateStart().toString());
        }catch(ParseException pe){
            sa.setDateStart(null);
            System.out.println("Error de Date: "+pe.getMessage());
            pe.printStackTrace();
        }
        return sa;
    }
}
