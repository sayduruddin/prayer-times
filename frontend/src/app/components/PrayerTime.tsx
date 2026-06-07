type PrayerTimes = {
	fajr: string;
	sunrise: string;
	dhuhr: string;
	asr: string;
	maghrib: string;
	isha: string;
};

type PrayerTimesMapProps = {
	prayerTimes: PrayerTimes;
};

export const PrayerTime = ({
	prayer,
	prayerTime,
}: {
	prayer: string;
	prayerTime: string;
}) => (
	<div className='bg-neutral-300 max-w-sm p-6 m-6 border border-default rounded-3xl shadow-sm transition-all hover:shadow-md'>
		<h5 className='mb-2 text-sm sm:text-lg font-medium text-neutral-600 text-center'>
			{prayer}
		</h5>
		<h5 className='text-sm sm:text-xl font-bold text-neutral-900 text-center tabular-nums'>
			{prayerTime}
		</h5>
	</div>
);

export const PrayerTimesMap = ({ prayerTimes }: PrayerTimesMapProps) => {
	console.log("prayer times", prayerTimes);
	return (
		<div className='grid grid-cols-2 max-w-md mx-auto'>
			{Object.entries(prayerTimes).map(([prayer, time]) => {
				return <PrayerTime key={prayer} prayer={prayer} prayerTime={time} />;
			})}
		</div>
	);
};
