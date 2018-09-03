CREATE VIEW vPatientData AS
 SELECT pat.PatientId, pat.Name, loc.Address, con.GeneralDiagnosis
 FROM Patients pat
 JOIN Locations loc ON pat.LocationId = loc.LocationId
 JOIN Conditions con ON pat.ConditionId = con.ConditionId;