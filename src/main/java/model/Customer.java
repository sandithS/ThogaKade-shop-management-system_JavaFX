package model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Customer {

    private String custID;
    private String title;
    private String name;
    private LocalDate dob;
    private double salary;
    private String address;
    private String city;
    private String province;
    private String postalCode;

}
