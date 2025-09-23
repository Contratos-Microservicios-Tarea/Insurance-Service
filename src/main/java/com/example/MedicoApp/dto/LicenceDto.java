package com.example.MedicoApp.dto;

import java.time.LocalDate;


public record LicenceDto (
  String Folio,
  String PatientID,
  String DoctorID,
  String Diagnosis,
  LocalDate StartDate,
  Integer Days,
  String Status
)
{

}