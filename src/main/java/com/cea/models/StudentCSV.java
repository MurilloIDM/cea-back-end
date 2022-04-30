package com.cea.models;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCSV {

    @CsvBindByName(column = "nome")
    private String name;
    @CsvBindByName(column = "email")
    private String email;
    @CsvBindByName(column = "telefone")
    private String phoneNumber;
    @CsvBindByName(column = "data_expiração")
    private String expirationDate;

}
