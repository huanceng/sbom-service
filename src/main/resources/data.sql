-- Init table product_type
INSERT INTO product_type VALUES('openEuler')
ON CONFLICT (type) DO NOTHING;

INSERT INTO product_type VALUES('MindSpore')
ON CONFLICT (type) DO NOTHING;

INSERT INTO product_type VALUES('openGauss')
ON CONFLICT (type) DO NOTHING;

-- Init table product_config

--openEuler
INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('013d61a1-5938-46db-9092-88df47c10bf6', 'version', '版本号', 'enum([{"label":"22.03-LTS","value":"22.03-LTS"}])', 1, 'openEuler')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('f0266c11-1d7a-45c6-80e6-2ccf586f6755', 'imageFormat', '文件格式', 'enum([{"label":"ISO","value":"ISO"}])', 2, 'openEuler')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('7f959c6b-6651-4c56-be30-5e2cebb901cf', 'imageType', '镜像类型', 'enum([{"label":"everything","value":"everything"},{"label":"normal","value":"empty"}])', 3, 'openEuler')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('5263c064-4d04-4232-a717-eb84499b5f5f', 'arch', '系统架构', 'enum([{"label":"aarch64","value":"aarch64"},{"label":"x86 64","value":"x86_64"}])', 4, 'openEuler')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;


-- MindSpore
INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('d96a0380-d9c0-4176-bbd6-9578952636f6', 'version', '版本号', 'enum([{"label":"1.8.0","value":"1.8.0"}])', 1, 'MindSpore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('a4e710c7-6811-4626-b32b-ae067fade540', 'platform', '硬件平台', 'enum([{"label":"CPU","value":"CPU"}])', 2, 'MindSpore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('327af62f-9689-4dd4-a1ff-b8ff7ad0bd61', 'os', '操作系统', 'enum([{"label":"Linux","value":"Linux"}])', 3, 'MindSpore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('75e5459b-b969-49e7-9d68-f2f346eb2120', 'arch', '系统架构', 'enum([{"label":"aarch64","value":"aarch64"},{"label":"x86 64","value":"x86_64"}])', 4, 'MindSpore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, label, value_type, ord, product_type)
VALUES('e471690e-69a1-4646-8868-036e53d702c6', 'language', '编程语言', 'enum([{"label":"Python 3.7.0","value":"Python 3.7.0"},{"label":"Python 3.8.0","value":"Python 3.8.0"},{"label":"Python 3.9.0","value":"Python 3.9.0"}])', 5, 'MindSpore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, label = EXCLUDED.label, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

-- openGauss


-- Insert openEuler products
INSERT INTO product(id, name, attribute)
VALUES('4a34d4b7-25ce-4c8d-b2f7-9d210a6dd32c', 'openEuler-22.03-LTS-x86_64-dvd.iso', '{"productType":"openEuler", "version":"22.03-LTS","imageFormat":"ISO","imageType":"empty","arch":"x86_64"}'::jsonb)
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, attribute = EXCLUDED.attribute;

INSERT INTO product(id, name, attribute)
VALUES('55dfefff-ec35-49f4-b395-de3824605bbc', 'openEuler-22.03-LTS-everything-x86_64-dvd.iso', '{"productType":"openEuler", "version":"22.03-LTS","imageFormat":"ISO","imageType":"everything","arch":"x86_64"}'::jsonb)
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, attribute = EXCLUDED.attribute;

INSERT INTO product(id, name, attribute)
VALUES('0810d78a-5404-45b1-93ba-dbcb17dfffe5', 'openEuler-22.03-LTS-netinst-aarch64-dvd.iso', '{"productType":"openEuler", "version":"22.03-LTS","imageFormat":"ISO","imageType":"netinst","arch":"aarch64"}'::jsonb)
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, attribute = EXCLUDED.attribute;

INSERT INTO product(id, name, attribute)
VALUES('e686d5ba-cd30-41e7-b97a-a3481bb6e0a2', 'mindspore-1.8.0-cp37-cp37m-linux_x86_64.whl', '{"productType": "MindSpore", "version": "1.8.0","platform": "CPU", "os": "Linux", "arch": "x86_64", "language": "Python 3.7.0"}'::jsonb)
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, attribute = EXCLUDED.attribute;
