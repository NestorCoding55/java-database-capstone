// doctorCard.js
import { deleteDoctor } from './services/doctorServices.js';
import { getPatientData } from './services/patientServices.js';
import { showBookingOverlay } from './loggedPatient.js';

export function createDoctorCard(doctor) {
    const card = document.createElement("div");
    card.classList.add("doctor-card");
    
    const role = localStorage.getItem("userRole");
    
    // Create doctor info section
    const infoDiv = document.createElement("div");
    infoDiv.classList.add("doctor-info");
    
    const name = document.createElement("h3");
    name.textContent = doctor.name;
    
    const specialization = document.createElement("p");
    specialization.textContent = `Specialty: ${doctor.specialty || 'Not specified'}`;
    
    const email = document.createElement("p");
    email.textContent = `Email: ${doctor.email}`;
    
    const availability = document.createElement("p");
    availability.textContent = `Available: ${doctor.availableTimes ? doctor.availableTimes.join(", ") : 'Not available'}`;
    
    infoDiv.appendChild(name);
    infoDiv.appendChild(specialization);
    infoDiv.appendChild(email);
    infoDiv.appendChild(availability);
    
    // Create actions container
    const actionsDiv = document.createElement("div");
    actionsDiv.classList.add("card-actions");
    
    // Conditionally add buttons based on role
    if (role === "admin") {
        const removeBtn = document.createElement("button");
        removeBtn.textContent = "Delete";
        removeBtn.classList.add("delete-btn");
        
        removeBtn.addEventListener("click", async () => {
            if (confirm(`Are you sure you want to delete Dr. ${doctor.name}?`)) {
                const token = localStorage.getItem("token");
                try {
                    const result = await deleteDoctor(doctor.id, token);
                    if (result.success) {
                        card.remove();
                        alert(`Doctor ${doctor.name} deleted successfully`);
                    } else {
                        alert(`Failed to delete doctor: ${result.message}`);
                    }
                } catch (error) {
                    alert(`Error deleting doctor: ${error.message}`);
                }
            }
        });
        
        actionsDiv.appendChild(removeBtn);
        
    } else if (role === "patient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("book-btn");
        
        bookNow.addEventListener("click", () => {
            alert("Please log in first to book an appointment.");
        });
        
        actionsDiv.appendChild(bookNow);
        
    } else if (role === "loggedPatient") {
        const bookNow = document.createElement("button");
        bookNow.textContent = "Book Now";
        bookNow.classList.add("book-btn");
        
        bookNow.addEventListener("click", async (e) => {
            const token = localStorage.getItem("token");
            if (!token) {
                alert("Session expired. Please log in again.");
                localStorage.removeItem("userRole");
                window.location.href = "/";
                return;
            }
            
            try {
                const patientData = await getPatientData(token);
                showBookingOverlay(e, doctor, patientData);
            } catch (error) {
                alert(`Error fetching patient data: ${error.message}`);
            }
        });
        
        actionsDiv.appendChild(bookNow);
    }
    
    // Assemble the card
    card.appendChild(infoDiv);
    card.appendChild(actionsDiv);
    
    return card;
}