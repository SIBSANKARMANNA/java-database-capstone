import { createDoctorCard } from "./components/doctorCard.js";
import { openModal } from "./components/modals.js";
import { getDoctors, filterDoctors } from "./services/doctorServices.js";
import {
  patientLogin,
  patientSignup
} from "./services/patientServices.js";

/* =========================
   LOAD DOCTORS ON PAGE LOAD
========================= */
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();

  const signupBtn = document.getElementById("patientSignup");
  const loginBtn = document.getElementById("patientLogin");

  if (signupBtn) signupBtn.addEventListener("click", () => openModal("patientSignup"));
  if (loginBtn) loginBtn.addEventListener("click", () => openModal("patientLogin"));

  document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
  document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
  document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);
});

/* =========================
   LOAD ALL DOCTORS
========================= */
async function loadDoctorCards() {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  try {
    const doctors = await getDoctors();

    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = "<p>No doctors available.</p>";
      return;
    }

    renderDoctorCards(doctors);
  } catch (error) {
    console.error(error);
    contentDiv.innerHTML = "<p>Error loading doctors.</p>";
  }
}

/* =========================
   FILTER DOCTORS
========================= */
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar").value || null;
  const time = document.getElementById("filterTime").value || null;
  const specialty = document.getElementById("filterSpecialty").value || null;

  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  try {
    const doctors = await filterDoctors(name, time, specialty);

    if (!doctors || doctors.length === 0) {
      contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
      return;
    }

    renderDoctorCards(doctors);
  } catch (error) {
    console.error(error);
    contentDiv.innerHTML = "<p>Error filtering doctors.</p>";
  }
}

/* =========================
   RENDER DOCTOR CARDS
========================= */
function renderDoctorCards(doctors) {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  doctors.forEach((doctor) => {
    const card = createDoctorCard(doctor);
    contentDiv.appendChild(card);
  });
}

/* =========================
   PATIENT SIGNUP
========================= */
window.signupPatient = async function () {
  const data = {
    name: document.getElementById("signupName").value,
    email: document.getElementById("signupEmail").value,
    password: document.getElementById("signupPassword").value,
    phone: document.getElementById("signupPhone").value,
    address: document.getElementById("signupAddress").value,
  };

  try {
    const result = await patientSignup(data);

    if (result.success) {
      alert(result.message || "Signup successful!");
      window.location.reload();
    } else {
      alert(result.message || "Signup failed");
    }
  } catch (error) {
    console.error(error);
    alert("Error during signup");
  }
};

/* =========================
   PATIENT LOGIN
========================= */
window.loginPatient = async function () {
  const data = {
    email: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value,
  };

  try {
    const response = await patientLogin(data);

    if (!response.ok) {
      alert("Invalid credentials");
      return;
    }

    const result = await response.json();
    localStorage.setItem("token", result.token);
    localStorage.setItem("userRole", "loggedPatient");

    window.location.href = "./loggedPatientDashboard.html";
  } catch (error) {
    console.error(error);
    alert("Login failed");
  }
};
