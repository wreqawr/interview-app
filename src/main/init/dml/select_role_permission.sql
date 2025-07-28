select tr.role_name, tr.description, tp.permission_code, tp.description
from t_role tr,
     t_role_permission trp,
     t_permission tp
where tr.role_id = trp.role_id
  and trp.permission_id = tp.permission_id;

select count(*)
from t_role_permission;