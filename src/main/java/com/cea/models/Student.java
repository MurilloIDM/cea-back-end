package com.cea.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String socialName;
    private String password;
    private String email;
    private String phoneNumber;
    private Date expirationDate;
    private Date updatedAt;

    public static Date getExpirationDate(String datePaymentStr) {
        Date dateNow = null;
        Date datePayment = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        try {
            Date datePaymentParse = dateFormat.parse(datePaymentStr);
            String datePaymentFormat = dateFormat.format(datePaymentParse);
            datePayment = dateFormat.parse(datePaymentFormat);

            String dateNowFormat = dateFormat.format(new Date());
            dateNow = dateFormat.parse(dateNowFormat);
        } catch (ParseException error) {
            return null;
        }

        Date expirationDate = null;
        if (datePayment != null && dateNow != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datePayment);
            calendar.add(Calendar.YEAR, 1);

            expirationDate = calendar.getTime();

            boolean isActive = studentIsActive(expirationDate, dateNow);
            if (!isActive) {
                expirationDate = null;
            }
        }

        return expirationDate;
    }

    public static boolean studentIsActive(Date expirationDate, Date dateNow) {
        if (expirationDate.compareTo(dateNow) < 0) {
            return false;
        }

        return true;
    }

}
