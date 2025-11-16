// adminDashboard.js
import { openModal } from '../components/modals.js';
import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { createDoctorCard } from './components/doctorCard.js';

// Event Binding for Add Doctor button
document.addEventListener('DOMContentLoaded', function() {
    const addDocBtn = document.getElementById('addDocBtn');
    if (addDocBtn) {
        addDocBtn.addEventListener('click', () => {
            openModal('addDoctor');
        });
    }

    // Load doctors on page load
    loadDoctorCards();

    // Set up search and filter event listeners
    document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
    document.getElementById("filterTime").addEventListener("change", filterDoctorsOnChange);
    document.getElementById("filterSpecialty").addEventListener("change", filterDoctorsOnChange);
});

// Load all doctor cards
async function loadDoctorCards() {
    try {
        const doctors = await getDoctors();
        renderDoctorCards(doctors);
    } catch (error) {
        console.error("Error loading doctors:", error);
    }
}

// Filter doctors based on search and filter inputs
async function filterDoctorsOnChange() {
    try {
        const name = document.getElementById("searchBar").value || null;
        const time = document.getElementById("filterTime").value || null;
        const specialty = document.getElementById("filterSpecialty").value || null;

        const doctors = await filterDoctors(name, time, specialty);
        
        if (doctors && doctors.length > 0) {
            renderDoctorCards(doctors);
        } else {
            const contentDiv = document.getElementById("content");
            contentDiv.innerHTML = "<p>No doctors found with the given filters.</p>";
        }
    } catch (error) {
        alert("Error filtering doctors. Please try again.");
        console.error("Filter error:", error);
    }
}

// Render doctor cards to the content area
function renderDoctorCards(doctors) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";

    if (doctors && doctors.length > 0) {
        doctors.forEach(doctor => {
            const card = createDoctorCard(doctor);
            contentDiv.appendChild(card);
        });
    } else {
        contentDiv.innerHTML = "<p>No doctors available.</p>";
    }
}

// Global function to handle adding a new doctor
window.adminAddDoctor = async function() {
    // Collect form data
    const name = document.getElementById('doctorName').value;
    const email = document.getElementById('doctorEmail').value;
    const phone = document.getElementById('doctorPhone').value;
    const password = document.getElementById('doctorPassword').value;
    const specialty = document.getElementById('doctorSpecialty').value;
    
    // Collect available times from checkboxes
    const availableTimes = [];
    const timeCheckboxes = document.querySelectorAll('input[name="availableTimes"]:checked');
    timeCheckboxes.forEach(checkbox => {
        availableTimes.push(checkbox.value);
    });

    // Get admin token
    const token = localStorage.getItem('token');
    if (!token) {
        alert('Admin authentication required. Please log in again.');
        return;
    }

    // Build doctor object
    const doctor = {
        name,
        email,
        phone,
        password,
        specialty,
        availableTimes
    };

    try {
        const result = await saveDoctor(doctor, token);
        
        if (result.success) {
            alert(result.message);
            // Close modal and refresh
            const modal = document.getElementById('modal');
            modal.style.display = 'none';
            loadDoctorCards(); // Refresh the doctor list
        } else {
            alert('Failed to add doctor: ' + result.message);
        }
    } catch (error) {
        alert('Error adding doctor: ' + error.message);
        console.error('Add doctor error:', error);
    }
};