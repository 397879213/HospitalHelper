<?php

// Must exist for DataGenerator to work (change path if necessary)
require_once('/home/tommy/dev/IdeaProjects/Assignment3/Faker/src/autoload.php');

function check($str) 
{
	$str = str_replace("\n", "", $str);
	$str = str_replace("\r", "", $str);
	$str = str_replace(",", "", $str);
	return $str;
}

$faker = Faker\Factory::create();

$filename = $argv[1];
$numTuples = $argv[2];

$file = fopen($filename, 'w');

for ($i = 0; $i < $numTuples; ++$i)
{
	// Patient->Name
	fwrite($file, check($faker->name) . ', ');

	// Patient->Location->Address
	fwrite($file, check($faker->address) . ', ');

	// Patient->Location->State
	fwrite($file, check($faker->state) . ', ');

	// Patient->Location->Postcode
	fwrite($file, check($faker->postcode) . ', ');

	// Patient->Patient_Phones->MobilePhone
	fwrite($file, check($faker->phoneNumber) . ', ');

	// Patient->Patient_Phones->HomePhone
    fwrite($file, check($faker->phoneNumber) . ', ');

	// Patient->Patient_Phones->ICEPhone
	fwrite($file, check($faker->phoneNumber) . ', ');

	// Hospitals->PrimaryPhone
	fwrite($file, check($faker->phoneNumber) . ', ');

	// Hospitals->TollFreePhoneNum
	fwrite($file, check($faker->tollFreePhoneNumber) . ', ');

	// Hospitals->Location->Address
	fwrite($file, check($faker->address) . ', ');

	// Hospitals->Location->State
	fwrite($file, check($faker->state) . ', ');

	// Hospitals->Location->Postcode
	fwrite($file, check($faker->postcode) . ', ');

	// Doctors->Name
	fwrite($file, check($faker->name) . ', ');

	// Doctors->MobilePhone
	fwrite($file, check($faker->phoneNumber) . ', ');

	// Patients->CreditCards->CreditCardType
	fwrite($file, check($faker->creditCardType) . ', ');

	// Patients->CreditCards->CreditCardNum
	fwrite($file, $faker->creditCardNumber . ', ');

	// Patients->CreditCards->CreditCardExpDate
	fwrite($file, $faker->creditCardExpirationDateString . ', ');

	// Hospitals->Name
	fwrite($file, $faker->lastName);	


	fwrite($file, PHP_EOL);
}

fclose($file);

?>

