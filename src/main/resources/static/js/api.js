async function apiFetch(url,options){
  options=options||{};
  var token=getToken();
  var headers={'Content-Type':'application/json'};
  if(token)headers['Authorization']='Bearer '+token;
  Object.assign(headers,options.headers||{});
  var res=await fetch(url,Object.assign({},options,{headers:headers}));
  if(res.status===401){logout();return null}
  if(res.status===403){window.location.href='/index.html';return null}
  var ct=res.headers.get('Content-Type')||'';
  var isJson=ct.includes('application/json');
  if(!res.ok){
    var err=isJson?await res.json():null;
    throw new Error((err&&(err.error||err.message))||'HTTP '+res.status);
  }
  if(res.status===204)return null;
  return isJson?res.json():res.text();
}
