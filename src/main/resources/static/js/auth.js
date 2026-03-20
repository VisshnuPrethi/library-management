function getToken(){return localStorage.getItem('token')}
function getRole(){return localStorage.getItem('role')}
function getUsername(){return localStorage.getItem('username')}
function logout(){localStorage.removeItem('token');localStorage.removeItem('username');localStorage.removeItem('role');window.location.href='/login.html'}
function requireAuth(){if(!getToken())window.location.href='/login.html'}
function requireRole(r){if(!getToken()){window.location.href='/login.html';return}if(getRole()!==r)window.location.href='/index.html'}
function escapeHtml(s){if(s==null)return '';return String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;').replace(/'/g,'&#39;')}
function updateNav(){
  var token=getToken(),role=getRole(),username=getUsername();
  var loginLink=document.getElementById('loginLink');
  var registerLink=document.getElementById('registerLink');
  var logoutBtn=document.getElementById('logoutBtn');
  var myReqLink=document.getElementById('myRequestsLink');
  var adminLink=document.getElementById('adminLink');
  var userDisplay=document.getElementById('usernameDisplay');
  if(token){
    if(loginLink)loginLink.style.display='none';
    if(registerLink)registerLink.style.display='none';
    if(logoutBtn)logoutBtn.style.display='inline-block';
    if(myReqLink)myReqLink.style.display='inline';
    if(userDisplay)userDisplay.textContent=username||'';
    if(adminLink&&role==='LIBRARIAN')adminLink.style.display='inline';
  }
}
