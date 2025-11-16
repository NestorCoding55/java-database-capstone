// doctorDashboard.js
import { getAllAppointments } from './services/appointmentRecordService.js';
import { createPatientRow } from './components/patientRows.js';

// Initialize Global Variables
const tableBody = document.getElementById('patientTableBody');
let selectedDate = new Date().toISOString().split('T')[0]; // Today's date in YYYY-MM-DD
const token = localStorage.getItem('token');
let patientName = null;

// Setup Search Bar Functionality
document.getElementById('searchBar').addEventListener('input', (event) => {
    const searchValue = event.target.value.trim();
    patientName = searchValue ? searchValue : "null";
    loadAppointments();
});

// Bind Event Listeners to Filter Controls
document.getElementById('todayButton').addEventListener('click', () => {
    selectedDate = new Date().toISOString().split('T')[0];
    document.getElementById('datePicker').value = selectedDate;
    loadAppointments();
});

document.getElementById('datePicker').addEventListener('change', (event) => {
    selectedDate = event.target.value;
    loadAppointments();
});

// Define loadAppointments Function
async function loadAppointments() {
    try {
        const appointments = await getAllAppointments(selectedDate, patientName, token);
        
        // Clear existing content
        tableBody.innerHTML = '';
        
        if (!appointments || appointments.length === 0) {
            const noAppointmentsRow = document.createElement('tr');
            noAppointmentsRow.innerHTML = `
                <td colspan="5" class="noPatientRecord">
                    No Appointments found for ${selectedDate === new Date().toISOString().split('T')[0] ? 'today' : 'selected date'}.
                </td>
            `;
            tableBody.appendChild(noAppointmentsRow);
            return;
        }
        
        // Create rows for each appointment
        appointments.forEach(appointment => {
            const patient = {
                id: appointment.patientId,
                name: appointment.patientName,
                phone: appointment.patientPhone,
                email: appointment.patientEmail
            };
            
            const row = createPatientRow(patient, appointment);
            tableBody.appendChild(row);
        });
        
    } catch (error) {
        console.error('Error loading appointments:', error);
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="noPatientRecord">
                    Error loading appointments. Try again later.
                </td>
            </tr>
        `;
    }
}

// Initial Render on Page Load
document.addEventListener('DOMContentLoaded', function() {
    // Set date picker to today's date
    document.getElementById('datePicker').value = selectedDate;
    
    // Load today's appointments
    loadAppointments();
});