// modals.js

// Modal functionality
export function openModal(modalType) {
    console.log('Opening modal:', modalType);
    const modal = document.getElementById('modal');
    const modalBody = document.getElementById('modal-body');
    
    if (!modal || !modalBody) {
        console.error('Modal elements not found');
        return;
    }
    
    // Set modal content based on type
    if (modalType === 'adminLogin') {
        modalBody.innerHTML = `
            <h3>Admin Login</h3>
            <input type="text" id="adminUsername" placeholder="Username" class="modal-input">
            <input type="password" id="adminPassword" placeholder="Password" class="modal-input">
            <button onclick="adminLoginHandler()" class="modal-btn">Login</button>
        `;
    } else if (modalType === 'doctorLogin') {
        modalBody.innerHTML = `
            <h3>Doctor Login</h3>
            <input type="email" id="doctorEmail" placeholder="Email" class="modal-input">
            <input type="password" id="doctorPassword" placeholder="Password" class="modal-input">
            <button onclick="doctorLoginHandler()" class="modal-btn">Login</button>
        `;
    } else if (modalType === 'patientLogin') {
        modalBody.innerHTML = `
            <h3>Patient Login</h3>
            <input type="email" id="patientEmail" placeholder="Email" class="modal-input">
            <input type="password" id="patientPassword" placeholder="Password" class="modal-input">
            <button onclick="loginPatient()" class="modal-btn">Login</button>
        `;
    } else if (modalType === 'patientSignup') {
        modalBody.innerHTML = `
            <h3>Patient Sign Up</h3>
            <input type="text" id="name" placeholder="Full Name" class="modal-input">
            <input type="email" id="email" placeholder="Email" class="modal-input">
            <input type="password" id="password" placeholder="Password" class="modal-input">
            <input type="text" id="phone" placeholder="Phone" class="modal-input">
            <input type="text" id="address" placeholder="Address" class="modal-input">
            <button onclick="signupPatient()" class="modal-btn">Sign Up</button>
        `;
    } else if (modalType === 'addDoctor') {
        modalBody.innerHTML = `
            <h3>Add New Doctor</h3>
            <input type="text" id="doctorName" placeholder="Doctor Name" class="modal-input">
            <input type="email" id="doctorEmail" placeholder="Email" class="modal-input">
            <input type="password" id="doctorPassword" placeholder="Password" class="modal-input">
            <input type="text" id="doctorPhone" placeholder="Phone" class="modal-input">
            <input type="text" id="doctorSpecialty" placeholder="Specialty" class="modal-input">
            <button onclick="adminAddDoctor()" class="modal-btn">Add Doctor</button>
        `;
    } else {
        modalBody.innerHTML = `<h3>${modalType}</h3><p>Modal content for ${modalType}</p>`;
    }
    
    modal.style.display = 'block';
}

export function closeModal() {
    const modal = document.getElementById('modal');
    if (modal) {
        modal.style.display = 'none';
    }
}

// Close modal when clicking X
document.addEventListener('DOMContentLoaded', function() {
    const closeBtn = document.getElementById('closeModal');
    if (closeBtn) {
        closeBtn.onclick = closeModal;
    }
});

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        closeModal();
    }
});

// Make functions globally available for onclick handlers
window.openModal = openModal;
window.closeModal = closeModal;