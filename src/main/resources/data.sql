-- 공항 초기 데이터
INSERT INTO airports (airport_id, airport_name_ko, airport_name_en, country, city, is_deleted)
VALUES
    ('ICN', '인천국제공항', 'Incheon International Airport', '대한민국', '서울', FALSE),
    ('GMP', '김포국제공항', 'Gimpo International Airport', '대한민국', '서울', FALSE),
    ('CJU', '제주국제공항', 'Jeju International Airport', '대한민국', '제주', FALSE),
    ('PUS', '김해국제공항', 'Gimhae International Airport', '대한민국', '부산', FALSE),
    ('NRT', '나리타국제공항', 'Narita International Airport', '일본', '도쿄', FALSE)
ON CONFLICT (airport_id) DO NOTHING;
