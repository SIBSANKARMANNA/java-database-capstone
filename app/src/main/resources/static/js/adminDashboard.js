import { openModal } from "./components/modals.js";
import {
  getDoctors,
  filterDoctors,
  saveDoctor,
} from "./services/doctorServices.js";
import { createDoctorCard } from "./components/doctorCard.js";

/* =========================
   LOAD DOCTORS ON PAGE LOAD
========================= */
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorCards();

  const searchBar = document.getElementById("searchBar");
  const filterTime = document.getElementById("filterTime");
  const filterSpecialty = document.getElementById("filterSpecialty");

  if (searchBar)
    searchBar.addEventListener("input", filterDoctorsOnChange);
  if (filterTime)
    filterTime.addEventListener("change", filterDoctorsOnChange);
  if (filterSpecialty)
    filterSpecialty.addEventListener("change", filterDoctorsOnChange);
});

/* =========================
   OPEN ADD DOCTOR MODAL
========================= */
document.addEventListener("click", (e) => {
  if (e.target && e.target.id === "addDocBtn") {
    openModal("addDoctor");
  }
});

/* =========================
   LOAD ALL DOCTORS
========================= */
async function loadDoctorCards() {
  const contentDiv = document.getElementById("content");
  contentDiv.innerHTML = "";

  const doctors = await getDoctors();

  if (!doctors || doctors.length === 0) {
    contentDiv.innerHTML = "<p>No doctors found</p>";
    return;
  }

  renderDoctorCards(doctors);
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
   FILTER DOCTORS
========================= */
async function filterDoctorsOnChange() {
  const name = document.getElementById("searchBar")?.value || null;
  const time = document.getElementById("filterTime")?.value || null;
  const specialty =
    document.getElementById("filterSpecialty")?.value || null;

  const doctors = await filterDoctors(name, time, specialty);

  if (!doctors || doctors.length === 0) {
    document.getElementById("content").innerHTML =
      "<p>No doctors found with given filters</p>";
    return;
  }

  renderDoctorCards(doctors);
}

/* =========================
   ADD DOCTOR (MODAL FORM)
========================= */
window.adminAddDoctor = async function () {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Unauthorized access");
    return;
  }

  const name = document.getElementById("name").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const phone = document.getElementById("phone").value;
  const specialty = document.getElementById("specialty").value;

  const availability = [];
  document
    .querySelectorAll("input[name='availability']:checked")
    .forEach((cb) => availability.push(cb.value));

  const doctor = {
    name,
    email,
    password,
    phone,
    specialty,
    availability,
  };

  const response = await saveDoctor(doctor, token);

  if (response.success) {
    alert("Doctor added successfully");
    window.location.reload();
  } else {
    alert(response.message || "Failed to add doctor");
  }
};
