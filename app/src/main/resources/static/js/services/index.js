// index.js
import { openModal } from '../components/modals.js';
import { API_BASE_URL } from '../config/config.js';

const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login';

console.log('index.js loaded');

// Set up event listeners when DOM loads
document.addEventListener('DOMContentLoaded', function() {
    console.log('index.js: DOM loaded');
    
    const adminBtn = document.getElementById('adminLogin');
    const doctorBtn = document.getElementById('doctorLogin');
    const patientLoginBtn = document.getElementById('patientLogin');
    const patientSignupBtn = document.getElementById('patientSignup');

    if (adminBtn) {
        adminBtn.addEventListener('click', () => {
            console.log('Admin login clicked');
            openModal('adminLogin');
        });
    }

    if (doctorBtn) {
        doctorBtn.addEventListener('click', () => {
            console.log('Doctor login clicked');
            openModal('doctorLogin');
        });
    }
    
    if (patientLoginBtn) {
        patientLoginBtn.addEventListener('click', () => {
            console.log('Patient login clicked');
            openModal('patientLogin');
        });
    }
    
    if (patientSignupBtn) {
        patientSignupBtn.addEventListener('click', () => {
            console.log('Patient signup clicked');
            openModal('patientSignup');
        });
    }
});

// Admin login handler
window.adminLoginHandler = async function() {
    console.log('Admin login handler called');
    const username = document.getElementById('adminUsername')?.value;
    const password = document.getElementById('adminPassword')?.value;
    
    if (!username || !password) {
        alert('Please enter both username and password');
        return;
    }
    
    const admin = { username, password };
    console.log('Admin login attempt:', username);

    try {
        const response = await fetch(ADMIN_API, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(admin)
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            selectRole('admin');
        } else {
            alert('Invalid admin credentials!');
        }
    } catch (error) {
        alert('Login failed. Please try again.');
        console.error('Admin login error:', error);
    }
};

// Doctor login handler
window.doctorLoginHandler = async function() {
    console.log('Doctor login handler called');
    const email = document.getElementById('doctorEmail')?.value;
    const password = document.getElementById('doctorPassword')?.value;
    
    if (!email || !password) {
        alert('Please enter both email and password');
        return;
    }
    
    const doctor = { email, password };
    console.log('Doctor login attempt:', email);

    try {
        const response = await fetch(DOCTOR_API, {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(doctor)
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('token', data.token);
            selectRole('doctor');
        } else {
            alert('Invalid doctor credentials!');
        }
    } catch (error) {
        alert('Login failed. Please try again.');
        console.error('Doctor login error:', error);
    }
};

// Role selection function
window.selectRole = function(role) {
    console.log('Role selected:', role);
    localStorage.setItem('userRole', role);
    
    if (role === 'admin') {
        const token = localStorage.getItem('token');
        if (token) {
            window.location.href = `/adminDashboard/${token}`;
        } else {
            openModal('adminLogin');
        }
    } else if (role === 'doctor') {
        const token = localStorage.getItem('token');
        if (token) {
            window.location.href = `/doctorDashboard/${token}`;
        } else {
            openModal('doctorLogin');
        }
    } else if (role === 'patient') {
        window.location.href = '/pages/patientDashboard.html';
    }
};