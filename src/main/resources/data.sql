-- Init table product_type
INSERT INTO product_type VALUES('openeuler')
ON CONFLICT (type) DO NOTHING;

INSERT INTO product_type VALUES('mindspore')
ON CONFLICT (type) DO NOTHING;

INSERT INTO product_type VALUES('opengauss')
ON CONFLICT (type) DO NOTHING;

-- Init table product_config

--openeuler


-- mindspore
INSERT INTO product_config(id, name, value_type, ord, product_type)
VALUES('d96a0380-d9c0-4176-bbd6-9578952636f6', 'version', 'string', 1, 'mindspore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, value_type, ord, product_type)
VALUES('a4e710c7-6811-4626-b32b-ae067fade540', 'platform', 'string', 2, 'mindspore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, value_type, ord, product_type)
VALUES('327af62f-9689-4dd4-a1ff-b8ff7ad0bd61', 'os', 'string', 3, 'mindspore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, value_type, ord, product_type)
VALUES('75e5459b-b969-49e7-9d68-f2f346eb2120', 'arch', 'string', 4, 'mindspore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, value_type, ord, product_type)
VALUES('e471690e-69a1-4646-8868-036e53d702c6', 'language', 'string', 5, 'mindspore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

INSERT INTO product_config(id, name, value_type, ord, product_type)
VALUES('d66b4957-a986-422e-b644-758f099a6d73', 'language_version', 'string', 6, 'mindspore')
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, value_type = EXCLUDED.value_type, ord = EXCLUDED.ord, product_type = EXCLUDED.product_type;

-- opengauss


-- Insert openeuler products
INSERT INTO product(id, name, attribute)
VALUES('4a34d4b7-25ce-4c8d-b2f7-9d210a6dd32c', 'openEuler-22.03-LTS-x86_64-dvd.iso', '{"a": "b"}'::jsonb)
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, attribute = EXCLUDED.attribute;

INSERT INTO product(id, name, attribute)
VALUES('55dfefff-ec35-49f4-b395-de3824605bbc', 'openEuler-22.03-LTS-everything-x86_64-dvd.iso', '{"b": "a"}'::jsonb)
ON CONFLICT (id) DO UPDATE
    SET name = EXCLUDED.name, attribute = EXCLUDED.attribute;