import { API_BASE_URL } from "../config/config.js";

const PATIENT_API = API_BASE_URL + "/patient";

/* =========================
   PATIENT SIGNUP
========================= */
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}/signup`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("Error during patient signup:", error);
    return { success: false, message: "Signup failed" };
  }
}

/* =========================
   PATIENT LOGIN
========================= */
export async function patientLogin(data) {
  try {
    const response = await fetch(`${PATIENT_API}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    return response;
  } catch (error) {
    console.error("Error during patient login:", error);
    throw error;
  }
}

/* =========================
   GET LOGGED-IN PATIENT DATA
========================= */
export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/profile`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error("Failed to fetch patient data");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching patient data:", error);
    return null;
  }
}

/* =========================
   GET PATIENT APPOINTMENTS
========================= */
export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(
      `${PATIENT_API}/appointments/${id}?user=${user}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      throw new Error("Failed to fetch appointments");
    }

    return await response.json();
  } catch (error) {
    console.error("Error fetching appointments:", error);
    return null;
  }
}

/* =========================
   FILTER APPOINTMENTS
========================= */
export async function filterAppointments(condition, name, token) {
  try {
    const params = new URLSearchParams();

    if (condition) params.append("condition", condition);
    if (name) params.append("name", name);

    const response = await fetch(
      `${PATIENT_API}/appointments/filter?${params.toString()}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      throw new Error("Failed to filter appointments");
    }

    return await response.json();
  } catch (error) {
    console.error("Error filtering appointments:", error);
    alert("Something went wrong while filtering appointments");
    return [];
  }
}
