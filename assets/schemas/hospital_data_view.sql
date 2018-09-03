CREATE VIEW vHospitalData AS
 SELECT hos.HospitalId, hos.Name hosName, hos.TollFreePhoneNum, loc.Address
 FROM Hospitals hos
 JOIN Locations loc ON hos.LocationId = loc.LocationId;