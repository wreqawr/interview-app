select tu.user_id,
       tu.username,
       tu.password,
       tu.nickname,
       tu.email,
       tu.status,
       tu.created_at,
       tr.role_id,
       tr.role_name,
       tr.description,
       tp.permission_id,
       tp.permission_code,
       tp.description,
       tc.company_id,
       tc.company_name
from t_user tu
         left join t_user_role tur on tu.user_id = tur.user_id
         left join t_role tr on tr.role_id = tur.role_id
         left join t_role_permission trp on tur.role_id = trp.role_id
         left join t_permission tp on trp.permission_id = tp.permission_id
         left join t_user_company tuc on tu.user_id = tuc.user_id
         left join t_company tc on tuc.company_id = tc.company_id