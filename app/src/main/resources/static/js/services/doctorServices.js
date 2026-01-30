import { API_BASE_URL } from "../config/config.js";

const DOCTOR_API = API_BASE_URL + "/doctor";

/* =========================
   GET ALL DOCTORS
========================= */
export async function getDoctors() {
  try {
    const response = await fetch(DOCTOR_API);

    if (!response.ok) {
      throw new Error("Failed to fetch doctors");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching doctors:", error);
    return [];
  }
}

/* =========================
   DELETE DOCTOR (ADMIN)
========================= */
export async function deleteDoctor(id, token) {
  try {
    const response = await fetch(`${DOCTOR_API}/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error deleting doctor:", error);
    return { success: false, message: "Delete failed" };
  }
}

/* =========================
   SAVE / ADD DOCTOR (ADMIN)
========================= */
export async function saveDoctor(doctor, token) {
  try {
    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(doctor),
    });

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error saving doctor:", error);
    return { success: false, message: "Doctor creation failed" };
  }
}

/* =========================
   FILTER DOCTORS
========================= */
export async function filterDoctors(name, time, specialty) {
  try {
    const params = new URLSearchParams();

    if (name) params.append("name", name);
    if (time) params.append("time", time);
    if (specialty) params.append("specialty", specialty);

    const response = await fetch(`${DOCTOR_API}/filter?${params.toString()}`);

    if (!response.ok) {
      throw new Error("Failed to filter doctors");
    }

    return await response.json();
  } catch (error) {
    console.error("Error filtering doctors:", error);
    alert("Something went wrong while filtering doctors");
    return [];
  }
}
