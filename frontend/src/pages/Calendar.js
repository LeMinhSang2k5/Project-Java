import './Calendar.scss';

const Calendar = () => {
    return (
        <section className="vaccination-schedule">
            <h2>🗓️ Lịch tiêm chủng</h2>
            <form>
                <label htmlFor="fullname">Họ và tên:</label>
                <input type="text" id="fullname" name="fullname" required />

                <label htmlFor="dob">Ngày sinh:</label>
                <input type="date" id="dob" name="dob" required />

                <label htmlFor="vaccine">Loại vắc xin:</label>
                <select id="vaccine" name="vaccine" required>
                    <option value="">-- Chọn vắc xin --</option>
                    <option value="covid">COVID-19</option>
                    <option value="viemgan">Viêm gan B</option>
                    <option value="cum">Cúm</option>
                </select>

                <label htmlFor="date">Ngày tiêm dự kiến:</label>
                <input type="date" id="date" name="date" required />

                <button type="submit">Lưu lịch tiêm</button>
            </form>
        </section>
    );
};

export default Calendar;
