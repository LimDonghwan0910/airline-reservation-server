-- 기존 DB에 schedules.aircraft_id 타입이 숫자형인 경우 VARCHAR(10)으로 변경
ALTER TABLE schedules
    ALTER COLUMN aircraft_id TYPE VARCHAR(10) USING aircraft_id::text;

-- 공항 초기 데이터 (없을 경우)
INSERT INTO airports (airport_id, airport_name_ko, airport_name_en, country, city, is_deleted)
VALUES
    ('ICN', '인천국제공항', 'Incheon International Airport', '대한민국', '서울', FALSE),
    ('GMP', '김포국제공항', 'Gimpo International Airport', '대한민국', '서울', FALSE),
    ('CJU', '제주국제공항', 'Jeju International Airport', '대한민국', '제주', FALSE),
    ('PUS', '김해국제공항', 'Gimhae International Airport', '대한민국', '부산', FALSE),
    ('NRT', '나리타국제공항', 'Narita International Airport', '일본', '도쿄', FALSE),
    ('HND', '하네다국제공항', 'Tokyo Haneda International Airport', '일본', '도쿄', FALSE),
    ('KIX', '간사이국제공항', 'Kansai International Airport', '일본', '오사카', FALSE),
    ('FUK', '후쿠오카공항', 'Fukuoka Airport', '일본', '후쿠오카', FALSE),
    ('CTS', '신치토세공항', 'New Chitose Airport', '일본', '삿포로', FALSE)
ON CONFLICT (airport_id) DO NOTHING;
