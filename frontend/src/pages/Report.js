import './Report.scss'
const Report = () => {
    return (
        <div class="health-report">
            <h2>Báo cáo sự cố sức khỏe 📝</h2>
            <form>
                <label htmlFor="fullname">Họ và tên:</label>
                <input type="text" id="fullname" name="fullname" required />

                <label htmlFor="issue">Mô tả sự cố:</label>
                <textarea id="issue" name="issue" rows="5" required></textarea>

                <label htmlFor="date">Ngày xảy ra sự cố:</label>
                <input type="date" id="date" name="date" required />

                <button type="submit">Gửi báo cáo</button>
            </form>
        </div>
    );
};

export default Report;
