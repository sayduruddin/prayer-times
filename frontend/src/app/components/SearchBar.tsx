"use client";
import { useState } from "react";

type SearchBarProps = {
	onCoordinatesFound: (longitude: number, latitude: number) => void;
};

export default function SearchBar({ onCoordinatesFound }: SearchBarProps) {
	const [postcode, setPostcode] = useState<string>("");
	const [postcodeError, setPostcodeError] = useState<boolean>(false);
	const [validationError, setValidationError] = useState<boolean>(false);
	const [validationMessage, setValidationMessage] = useState<string>("");

	const postcodeSubmit = (e: React.SubmitEvent<HTMLFormElement>): void => {
		e.preventDefault();
		const UK_POSTCODE_REGEX: RegExp =
			/^([A-Za-z][A-Ha-hJ-Yj-y]?[0-9][A-Za-z0-9]? ?[0-9][A-Za-z]{2}|[Gg][Ii][Rr] ?0[Aa]{2})$/;

		const formData = new FormData(e.target);
		const postcodeFromFormData = formData.get("postcodeField") as string;
		const cleanPostcodeFromForm = postcodeFromFormData.trim().toUpperCase();

		if (!UK_POSTCODE_REGEX.test(cleanPostcodeFromForm)) {
			setValidationError(true);
			setValidationMessage("Incorrect postcode format. Please try again.");
			return;
		}

		setPostcode(postcodeFromFormData);
		fetchPostcodeData(postcodeFromFormData);
	};

	const fetchPostcodeData = async (postcode: string): Promise<void> => {
		console.log(`you entered ${postcode}`);
		try {
			const response = await fetch(
				`https://api.postcodes.io/postcodes/${postcode}`,
			);
			if (!response.ok) throw new Error("Postcode not found by API");
			const {
				result: { latitude, longitude },
			} = await response.json();
			onCoordinatesFound(longitude, latitude);
		} catch {
			console.error("error fetching data");
			setPostcodeError(true);
		}
	};

	return (
		<div>
			{validationError && validationMessage && (
				<p className='m-2 p-2 ring-2 ring-red-400 rounded-2xl'>
					{validationMessage}
				</p>
			)}
			{postcode && !postcodeError ? (
				<div className='flex items-center justify-between gap-4 w-full max-w-sm mx-auto p-2'>
					<p className='text-sm md:text-xl font-semibold text-center'>
						You entered {postcode}
					</p>
					<button
						className='px-2 py-2 bg-neutral-800 text-white rounded-xl text-sm font-semibold hover:bg-neutral-700'
						onClick={() => {
							setPostcode("");
						}}
						type='button'>
						Click to re-enter
					</button>
				</div>
			) : (
				<form
					onSubmit={postcodeSubmit}
					className='p-2 border border-emerald-500 rounded-3xl min-w-0 flex-1 flex items-center justify-between gap-2 bg-slate-800/10'>
					<input
						type='text'
						className='bg-transparent outline-none px-3 py-1 text-black flex-1 min-w-0'
						id='postcodeInput'
						name='postcodeField'
						placeholder='Enter UK postcode...'
						minLength={5}
						maxLength={8}
						required
					/>
					{/* Add a pattern/regex for the validation of input */}
					<button
						type='submit'
						className='bg-emerald-600 hover:bg-emerald-500 text-white p-2 rounded-2xl text-sm font-semibold shrink-0'>
						Submit
					</button>
				</form>
			)}
		</div>
	);
}
