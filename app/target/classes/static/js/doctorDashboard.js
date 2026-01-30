import { getAllAppointments } from "./services/appointmentRecordService.js";
import { createPatientRow } from "./components/patientRows.js";

/* =========================
   GLOBAL VARIABLES
========================= */
const tableBody = document.getElementById("patientTableBody");
const token = localStorage.getItem("token");

let selectedDate = new Date().toISOString().split("T")[0];
let patientName = null;

/* =========================
   EVENT LISTENERS
========================= */
document.addEventListener("DOMContentLoaded", () => {
  loadAppointments();

  const searchBar = document.getElementById("searchBar");
  const datePicker = document.getElementById("datePicker");
  const todayBtn = document.getElementById("todayButton");

  if (searchBar) {
    searchBar.addEventListener("input", (e) => {
      patientName = e.target.value || null;
      loadAppointments();
    });
  }

  if (datePicker) {
    datePicker.value = selectedDate;
    datePicker.addEventListener("change", (e) => {
      selectedDate = e.target.value;
      loadAppointments();
    });
  }

  if (todayBtn) {
    todayBtn.addEventListener("click", () => {
      selectedDate = new Date().toISOString().split("T")[0];
      datePicker.value = selectedDate;
      loadAppointments();
    });
  }
});

/* =========================
   LOAD APPOINTMENTS
========================= */
async function loadAppointments() {
  try {
    tableBody.innerHTML = "";

    const appointments = await getAllAppointments(
      selectedDate,
      patientName,
      token
    );

    if (!appointments || appointments.length === 0) {
      tableBody.innerHTML = `
        <tr>
          <td colspan="5" class="noPatientRecord">
            No appointments found for selected date
          </td>
        </tr>`;
      return;
    }

    appointments.forEach((appointment) => {
      const row = createPatientRow(appointment);
      tableBody.appendChild(row);
    });
  } catch (error) {
    tableBody.innerHTML = `
      <tr>
        <td colspan="5" class="noPatientRecord">
          Error loading appointments
        </td>
      </tr>`;
    console.error(error);
  }
}
