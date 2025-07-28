select tr.description, tp.description
from t_role tr,
     t_role_permission trp,
     t_permission tp
where tr.role_id = trp.role_id
  and trp.permission_id = tp.permission_id
  and tr.role_id = 2;

select count(*)
from t_role_permission;