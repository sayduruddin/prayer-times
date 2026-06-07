"use client";
import { useEffect, useState } from "react";
import SearchBar from "./components/SearchBar";
import { PrayerTimesMap } from "./components/PrayerTime";

type Coordinates = {
	longitude: number;
	latitude: number;
};

export default function Home() {
	const [postcodeData, setPostcodeData] = useState<Coordinates | null>(null);

	const handlePostcodeData = (longitude: number, latitude: number) => {
		console.log("long and lat from postcode", longitude, latitude);
		setPostcodeData({ longitude, latitude });
	};

	const prayerTimes = {
		fajr: "03:50",
		sunrise: "04:57",
		dhuhr: "13:06",
		asr: "18:36",
		maghrib: "21:14",
		isha: "22:20",
	};

	useEffect(() => {
		console.log("fetch prayer times");
		// call backend fetch function here with postcode data which is long/lat.
		// use the state value from above.

		/* 
      the way functions need to be called is
      const fetchData = async () => {}

      fetchData()
    
    */
	}, [postcodeData]);

	return (
		<>
			<main className='flex justify-center items-center p-12'>
				<h1 className='text-4xl font-bold text-center'>Prayer Times</h1>
			</main>
			<div className='flex flex-col items-center'>
				<h2 className='text-base md:text-xl text-center'>
					Enter your location
				</h2>
				<SearchBar onCoordinatesFound={handlePostcodeData} />
				<PrayerTimesMap prayerTimes={prayerTimes} />
			</div>
		</>
	);
}
