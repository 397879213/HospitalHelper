CREATE VIEW vDoctorData AS
    SELECT doc.DoctorId, doc.Name, doc.Phone, hos.Name hosName
    FROM Doctors doc
    JOIN Hospitals hos ON doc.HospitalId = hos.HospitalId;