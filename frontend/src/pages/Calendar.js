import './Calendar.scss';
import './VaccinationSchedule.scss';

const Calendar = () => {
    return (
        <div>
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
                        <option value="soi">Sởi</option>
                        <option value="viemgan">Viêm gan B</option>
                        <option value="cum">Cúm</option>
                        <option value="bailiet">Bại liệt</option>
                        <option value="piformod">Pfizer hoặc Moderna</option>
                        <option value="ho">Ho</option>
                    </select>

                    <label htmlFor="date">Ngày tiêm dự kiến:</label>
                    <input type="date" id="date" name="date" required />

                    <button type="submit">Lưu lịch tiêm</button>
                </form>
            </section>
            <div className="vaccination-schedule-container">
                <h2>Bảng lịch tiêm chủng</h2>
                <table>
                    <thead>
                        <tr>
                            <th>TT</th>
                            <th>Các bệnh truyền nhiễm có vắc-xin tại Việt Nam</th>
                            <th>Vắc-xin</th>
                            <th>Đối tượng sử dụng</th>
                            <th>Lịch tiêm/uống</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td rowSpan="2">1</td>
                            <td rowSpan="2">Bệnh viêm gan vi-rút B</td>
                            <td>Vắc-xin viêm gan B đơn giá</td>
                            <td>Trẻ sơ sinh</td>
                            <td>Liều sơ sinh: tiêm trong vòng 24 giờ sau khi sinh</td>
                        </tr>
                        <tr>
                            <td>Vắc-xin phối hợp có chứa thành phần viêm gan B</td>
                            <td>Trẻ em dưới 1 tuổi</td>
                            <td>
                                - Lần 1: khi trẻ đủ 2 tháng tuổi<br />
                                - Lần 2: ít nhất 1 tháng sau lần 1<br />
                                - Lần 3: ít nhất 1 tháng sau lần 2
                            </td>
                        </tr>

                        <tr>
                            <td>2</td>
                            <td>Bệnh lao</td>
                            <td>Vắc-xin lao</td>
                            <td>Trẻ em dưới 1 tuổi</td>
                            <td>Tiêm 1 lần cho trẻ trong vòng 1 tháng sau khi sinh</td>
                        </tr>

                        <tr>
                            <td rowSpan="2">3</td>
                            <td rowSpan="2">Bệnh bạch hầu</td>
                            <td>Vắc-xin phối hợp có chứa thành phần bạch hầu</td>
                            <td>Trẻ em dưới 1 tuổi</td>
                            <td>
                                - Lần 1: khi trẻ đủ 2 tháng tuổi<br />
                                - Lần 2: ít nhất 1 tháng sau lần 1<br />
                                - Lần 3: ít nhất 1 tháng sau lần 2
                            </td>
                        </tr>
                        <tr>
                            <td>Vắc-xin phối hợp có chứa thành phần bạch hầu</td>
                            <td>Trẻ em dưới 2 tuổi</td>
                            <td>Tiêm nhắc lại khi trẻ đủ 18 tháng tuổi</td>
                        </tr>

                        <tr>
                            <td rowSpan="2">4</td>
                            <td rowSpan="2">Bệnh ho gà</td>
                            <td>Vắc-xin phối hợp có chứa thành phần ho gà</td>
                            <td>Trẻ em dưới 1 tuổi</td>
                            <td>
                                - Lần 1: khi trẻ đủ 2 tháng tuổi<br />
                                - Lần 2: ít nhất 1 tháng sau lần 1<br />
                                - Lần 3: ít nhất 1 tháng sau lần 2
                            </td>
                        </tr>
                        <tr>
                            <td>Vắc-xin phối hợp có chứa thành phần ho gà</td>
                            <td>Trẻ em dưới 2 tuổi</td>
                            <td>Tiêm nhắc lại khi trẻ đủ 18 tháng tuổi</td>
                        </tr>

                        <tr>
                            <td rowSpan="2">5</td>
                            <td rowSpan="2">Bệnh bại liệt</td>
                            <td>Vắc-xin bại liệt uống đa giá</td>
                            <td>Trẻ em dưới 1 tuổi</td>
                            <td>
                                - Lần 1: khi trẻ đủ 2 tháng tuổi<br />
                                - Lần 2: ít nhất 1 tháng sau lần 1<br />
                                - Lần 3: ít nhất 1 tháng sau lần 2
                            </td>
                        </tr>
                        <tr>
                            <td>Vắc-xin bại liệt tiêm đa giá</td>
                            <td>Trẻ em dưới 1 tuổi</td>
                            <td>
                                - Mũi 1: khi trẻ đủ 5 tháng tuổi<br />
                                - Mũi 2: khi trẻ đủ 9 tháng tuổi
                            </td>
                        </tr>

                        <tr>
                            <td rowSpan="2">6</td>
                            <td rowSpan="2">Bệnh sởi</td>
                            <td>Vắc-xin sởi đơn giá</td>
                            <td>Trẻ em dưới 1 tuổi</td>
                            <td>Tiêm khi trẻ đủ 9 tháng tuổi</td>
                        </tr>
                        <tr>
                            <td>Vắc-xin phối hợp có chứa thành phần sởi</td>
                            <td>Trẻ em dưới 2 tuổi</td>
                            <td>Tiêm khi trẻ đủ 18 tháng tuổi</td>
                        </tr>

                        <tr>
                            <td>7</td>
                            <td>Bệnh do Covid 19</td>
                            <td>Vắc-xin Pfizer hoặc Moderna</td>
                            <td>Trẻ từ 6 tháng đến 11 tuổi
                                Người từ 12 tuổi trở lên
                                Trẻ suy giảm miễn dịch (6 tháng – 11 tuổi)
                            </td>
                            <td>
                                - 3 mũi: Mũi 1 và 2 cách nhau 3 tuần; mũi 3 cách mũi 2 ít nhất 8 tuần <br />
                                - 1 mũi:Nếu đã tiêm trước đó, mũi mới cách mũi gần nhất ít nhất 2 tháng<br />
                                - Ít nhất 3 mũi:Mỗi mũi cách nhau ít nhất 4–8 tuần
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

    );
};

export default Calendar;
