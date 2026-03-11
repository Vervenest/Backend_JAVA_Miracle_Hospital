// API Configuration
const API_BASE_URL = window.location.origin; // Uses same domain/port as frontend

document.getElementById('send').addEventListener('click', async () => {
  const mobile = document.getElementById('mobile').value || '9133302595';
  const out = document.getElementById('output');
  out.textContent = 'Sending...';
  try {
    const params = new URLSearchParams();
    params.append('mobileNo', mobile);
    const resp = await fetch(`${API_BASE_URL}/api/patients/patientLogin`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params.toString()
    });
    
    if (!resp.ok) {
      throw new Error(`API Error: ${resp.status} ${resp.statusText}`);
    }
    
    const json = await resp.json();
    out.textContent = JSON.stringify(json, null, 2);
  } catch (e) {
    out.textContent = 'Error: ' + e.message;
    console.error('API connection error:', e);
  }
});