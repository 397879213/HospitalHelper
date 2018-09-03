DROP TABLE IF EXISTS Conditions;
CREATE TABLE Conditions (ConditionId INT AUTO_INCREMENT, GeneralDiagnosis VARCHAR(150), SpecificDiagnosis VARCHAR(500), Severity INT, PRIMARY KEY (ConditionId));