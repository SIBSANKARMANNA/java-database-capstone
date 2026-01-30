import { openModal } from "../components/modals.js";
import { API_BASE_URL } from "../config/config.js";

/* API endpoints */
const ADMIN_API = API_BASE_URL + "/admin";
const DOCTOR_API = API_BASE_URL + "/doctor/login";

/* Attach button listeners after page load */
window.onload = function () {
  const adminBtn = document.getElementById("adminLogin");
  if (adminBtn) {
    adminBtn.addEventListener("click", () => {
      openModal("adminLogin");
    });
  }

  const doctorBtn = document.getElementById("doctorLogin");
  if (doctorBtn) {
    doctorBtn.addEventListener("click", () => {
      openModal("doctorLogin");
    });
  }
};

/* =========================
   ADMIN LOGIN HANDLER
========================= */
window.adminLoginHandler = async function () {
  try {
    const username = document.getElementById("adminUsername").value;
    const password = document.getElementById("adminPassword").value;

    const admin = { username, password };

    const response = await fetch(ADMIN_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(admin),
    });

    if (!response.ok) {
      alert("Invalid credentials!");
      return;
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);
    selectRole("admin");
  } catch (error) {
    alert("Something went wrong. Please try again.");
    console.error(error);
  }
};

/* =========================
   DOCTOR LOGIN HANDLER
========================= */
window.doctorLoginHandler = async function () {
  try {
    const email = document.getElementById("doctorEmail").value;
    const password = document.getElementById("doctorPassword").value;

    const doctor = { email, password };

    const response = await fetch(DOCTOR_API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(doctor),
    });

    if (!response.ok) {
      alert("Invalid credentials!");
      return;
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);
    selectRole("doctor");
  } catch (error) {
    alert("Something went wrong. Please try again.");
    console.error(error);
  }
};
