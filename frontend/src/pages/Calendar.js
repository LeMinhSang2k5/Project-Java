import "./Calendar.scss";
import nextIcon from "../assets/img/next-svgrepo-com.svg";
import { useState } from "react";

const Calendar = () => {
  const [selectedTab, setSelectedTab] = useState("lichTongQuat");

  const renderTable = () => {
    switch (selectedTab) {
      case "lichTongQuat":
        return <TableLichTongQuat />;
      case "17_19":
        return <Table17_19Tuoi />;
      case "19_49":
        return <Table19_49Tuoi />;
      case "dacBiet":
        return <TableTruongHopDacBiet />;
      default:
        return null;
    }
  };
  return (
    <div>
      <div className="SectionSchedual" id="lichtiemchung">
        <div className="cusContrainer">
          <h2 className="title">Lịch Tiêm Chủng</h2>
          <div className="listTabs">
            <div className="Tabscontent ">
              <div
                className={`iteamTabs ${
                  selectedTab === "lichTongQuat" ? "active-tab" : ""
                }`}
                onClick={() => setSelectedTab("lichTongQuat")}
              >
                <span className="tabs">Lịch tổng quát</span>
              </div>
              <div className="icon_img">
                <img src={nextIcon} alt="next icon" />
              </div>
              <div
                className={`iteamTabs ${
                  selectedTab === "17_19" ? "active-tab" : ""
                }`}
                onClick={() => setSelectedTab("17_19")}
              >
                <p className="tabs">17-19</p>
              </div>
              <div className="icon_img">
                <img src={nextIcon} alt="next icon" />
              </div>
              <div
                className={`iteamTabs ${
                  selectedTab === "19_49" ? "active-tab" : ""
                }`}
                onClick={() => setSelectedTab("19_49")}
              >
                <p className="tabs">19-49</p>
              </div>
              <div className="icon_img">
                <img src={nextIcon} alt="next icon" />
              </div>
              <div
                className={`iteamTabs ${
                  selectedTab === "dacBiet" ? "active-tab" : ""
                }`}
                onClick={() => setSelectedTab("dacBiet")}
              >
                <p className="tabs">Trường hợp đặc biệt</p>
              </div>
            </div>
          </div>
          <div className="ListTable">{renderTable()}</div>
        </div>
      </div>
    </div>
  );
};
const Table17_19Tuoi = () => {
  return (
    <div className="ListTable">
      <div className="tableContent">
        <table>
          <tr>
            <td className="background_cus_align">
              <div className="cus_align">
                <span>Bệnh</span>
              </div>
            </td>
            <td className="background_cus_align">
              <div className="cus_align">
                <span>Vắc-xin</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="cusCell">
                <span>17 tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="cusCell">
                <span>18 tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="cusCell">
                <span>19 tuổi</span>
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Viêm gan B</span>
              </div>
            </td>
            <td rowspan="6" className="background_vac-xin">
              <div className="vac-xin">
                <span>Vắc xin 6 trong 1</span>
              </div>
            </td>
            <td className="background_null">
              <div></div>
            </td>
            <td rowspan="6" className="background_hits">
              <div className="hits">
                <span>Mũi 4</span>
              </div>
            </td>
            <td className="background_null">
              <div></div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Bạch Hầu</span>
              </div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Uốn ván</span>
              </div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Ho gà</span>
              </div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Haemophilus Inflenzea tuýp B</span>
              </div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Bại liệt</span>
              </div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
            <td rowspan="0" hidden></td>
            <td className="background_null">
              <div></div>
            </td>
          </tr>
          <tr>
            <td rowspan="3" className="background_sicks">
              <div className="sicks">Viêm não Nhật Bản</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Lựa chọn 1: VNNB bất hoạt từ não chuột</span>
              </div>
            </td>
            <td className="background_null">
              <div></div>
            </td>
            <td className="grid-cell"></td>
            <td className="background_hits">
              <div className="hits">
                <span>Mũi 3 (tiêm nhắc mỗi 3 năm sau đó)</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowspan="0" hidden></td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span> Lựa chọn 2: VNNB bất hoạt từ tế bào Vero</span>
              </div>
            </td>
            <td className="background_null">
              <div></div>
            </td>
            <td className="grid-cell"></td>
            <td className="background_null">
              <div></div>
            </td>
          </tr>
          <tr>
            <td rowspan="0" hidden></td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Lựa chọn 3: VNNB sống giảm độc lực, tái tổ hợp</span>
              </div>
            </td>
            <td className="background_null">
              <div></div>
            </td>
            <td className="grid-cell"></td>
            <td className="background_hits">
              <div className="hits">
                <span>1 Mũi nhắc (tốt nhất 1-2 năm sau mũi đầu tiên)</span>
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Sởi</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Vắc xin Sởi, Quai bị, Rubella</div>
            </td>
            <td colSpan="3" className="background_hits">
              <div className="hits">
                <p>
                  <strong>Mũi 2</strong>"-Cách mũi 1 ít nhất 4 tuần"
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Não mô cầu</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                Vắc xin cộng hợp 4 nhóm huyết thanh ACWY
              </div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>2 Mũi</strong>-cách nhau 3 tháng
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
            <td className="background_hits">
              <div className="hits">
                <p>
                  Từ 2 tuổi trở lên <strong>1 Mũi</strong>-tiêm nhắc cho nhóm
                  nguy cơ cao{" "}
                </p>
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Bệnh Cúm mùa</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Vắc xin Cúm bất hoạt</div>
            </td>
            <td colSpan="3" className="background_hits">
              <div className="hits">
                <p>
                  Tiêm cúm lần đầu:<strong>2 Mũi</strong>-cách nhau ít nhất 4
                  tuần tiêm hằng năm
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Thủy Đậu</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Thủy Đậu</div>
            </td>
            <td colSpan="3" className="background_hits">
              <div className="hits">
                <p>
                  <strong>Mũi 2</strong>-cách mũi 1 từ 6 tuần- 3 tháng, tùy loại
                  vắc xin
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Viêm gan A(Hep A)</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Viêm gan A đơn giá/ phối hợp</div>
            </td>
            <td colSpan="3" className="background_hits">
              <div className="hits">
                <p>
                  <strong>2 Mũi</strong>-cách nhau 6-12 tháng
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Dại</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Dại</div>
            </td>
            <td colSpan="3" className="background_hits">
              <div className="hits">
                <p>
                  <strong>5 Mũi tiêm bắp </strong>hoặc 8 liều (4 ngay) tiêm
                  trong da sau phơi nhiễm, đã tiêm vắc xin dại trước đó :
                  <strong>2 Mũi</strong>
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Tả</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Tả</div>
            </td>
            <td className="background_null">
              <div></div>
            </td>
            <td className="background_null"></td>
            <td className="background_hits">
              <div className="hits">
                <p>
                  <strong>2 liều uống</strong> cách nhau 14 ngày
                </p>
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Thương hàn</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Thương hàn</div>
            </td>
            <td className="background_null">
              <div></div>
            </td>
            <td className="grid-cell"></td>
            <td className="background_hits">
              <div className="hits">
                <p>
                  <strong>1 Mũi</strong>
                </p>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div className="note">
        <div className="note_tabs">
          <h1 className="space">Chú Thích:</h1>
          <div className="note">
            <div className="circle1"></div>
            <div className="">Tiêm chủng mở rộng</div>
          </div>
          <div className="note">
            <div className="circle2"></div>
            <div className="">Vắc xin có phí</div>
          </div>
        </div>
      </div>
      <div className="btn_submit">
        <button className="btn">
          <p className="pbtn">Xem lịch tiêm chủng</p>
        </button>
      </div>
    </div>
  );
};

const Table19_49Tuoi = () => {
  return (
    <div className="ListTable">
      <div className="tableContent">
        <table>
          <tr>
            <td className="background_customer">
              <div className="customer">
                <span>Bệnh</span>
              </div>
            </td>
            <td className="background_customer">
              <div className="customer">
                <span>Vắc-xin</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="cusCell">
                <span>19-26 tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="cusCell">
                <span>27-49 tuổi</span>
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Bệnh cúm mùa</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Vắc xin Cúm bất hoạt IV4</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  Tiêm chủng hàng năm<strong>1 Mũi</strong>
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Bạch hầu - Uốn ván - Ho gà</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                Vắc-xin phối hợp có chứa thành phần Bạch hầu & Ho gà giảm liều
              </div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>1 Mũi</strong>Tdap sau đó nhắc lại Tdap mỗi 10 năm
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Phế cầu</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">PCV</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>1 Mũi</strong>
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td rowspan="2" className="background_sicks">
              <div className="sicks">Viêm não Nhật Bản</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">VNNB Bất hoạt tế bào Vero</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>Mũi 2</strong>cách nhau 28 ngày + 1 mũi (mũi 3) sau 1
                  năm
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_vac-xin">
              <div className="vac-xin">VNNB Sống giảm độc lực</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>1 Mũi</strong>
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td rowspan="2" className="background_sicks">
              <div className="sicks">Não mô cầu</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                NMC_nhóm B có 4 kháng nguyên (4CMenB)
              </div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>2 Mũi</strong> - cách nhau 1 tháng
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_vac-xin">
              <div className="vac-xin">NMC_ACWY cộng hợp</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>1 Mũi</strong>(tới 55 tuổi)
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Sởi - quai bị - Rubellla</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">MMR</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>2 Mũi</strong> - cách nhau 1 tháng
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Thủy đậu</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Thủy đậu</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>2 Mũi</strong>- cách nhau 1 tháng
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Viêm gan B</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">VGB đơn giá</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>3 Mũi</strong> - 0,2,6 tháng -
                  <strong>CẦN TEST TRƯỚC KHI TIÊM</strong>{" "}
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Viêm gan A</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                Vắc-xin Viêm gan A đơn giá hoặc Vắc-xin phối hợp ngừa VGA và VGB
              </div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>
                    VGA ĐƠN - 2 MŨI (0,6 THÁNG), VGA PHỐI HỢP - 3 MŨI (0,1,6
                    THÁNG)
                  </strong>
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                Ung thư cổ tử cung và các bệnh khác do HPV
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">HPV</div>
            </td>
            <td className="background_hits">
              <div className="hits">
                <p>
                  <strong>3 Mũi</strong>- 0,2,6 tháng
                </p>
              </div>
            </td>
            <td className="background_null"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Dại</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Dại</div>
            </td>
            <td colSpan="2" className="background_supper">
              <div className="supper">
                <p>
                  <strong>
                    5 MŨI TIÊN BẮP HOẶC 8 MŨI TIÊM (4 NGÀY 2 VỊ TRÍ){" "}
                  </strong>
                  - sau phơi nhiễm. Nếu đã tiêm vắc xin dại trước đó, tiêm 2 mũi
                  ngày 0-3
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Thương hàn</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Thương hàn</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>1 Mũi</strong>- nhắc lại mỗi 3 năm nếu có phơi nhiễm
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">Sốt vàng</div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Sốt vàng</div>
            </td>
            <td colSpan="2" className="background_hits">
              <div className="hits">
                <p>
                  <strong>1 Mũi</strong>- nhắc lại mỗi 10 năm nếu sống trong
                  vùng dịch lưu hành
                </p>
              </div>
            </td>
            <td hidden colspan="0"></td>
          </tr>
        </table>
      </div>
      <div className="note">
        <div className="note_tabs">
          <h1 className="space">Chú Thích:</h1>
          <div className="note">
            <div className="circle1"></div>
            <div className="">Khuyến cáo cho nhóm nguy cơ</div>
          </div>
          <div className="note">
            <div className="circle2"></div>
            <div className="">Khuyến cáo theo tuổi cho nhóm chưa tiêm đủ</div>
          </div>
          <div className="note">
            <div className="circle3"></div>
            <div className="">Khuyến cáo sau khi bị phơi nhiễm</div>
          </div>
        </div>
      </div>
      <div className="btn_submit">
        <button className="btn">
          <p className="pbtn">Xem lịch tiêm chủng</p>
        </button>
      </div>
    </div>
  );
};

const TableLichTongQuat = () => {
  return (
    <div className="ListTable">
      <div className="tableContent">
        <table>
          <tr>
            <td className="background_cus">
              <div className="cus">
                <span>Tuổi tiêm</span>
              </div>
            </td>
            <td className="background_cus">
              <div className="cus">
                <span>Bệnh cần tiêm phòng</span>
              </div>
            </td>
            <td className="background_cus">
              <div className="cus">
                <span>Vắc xin</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="3" className="background_ages">
              <div className="ages">
                <span>Từ 24 tháng trở lên</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Thương hàn</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>
                  Thương hàn 1 mũi (Nhắc lại mỗi 3 năm nếu đi vào vùng dịch)
                </span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Viêm não Nhật Bản</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>VNNB JEVAX mũi 3</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Não mô cầu nhóm huyết thanh B</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>
                  Vắc xin não mô cầu nhóm B (4CMenB: 2 liều cách nhau 1 tháng)
                </span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="2" className="background_ages">
              <div className="ages">
                <span>Từ 4-7 tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Bạch hầu - Uốn ván - Ho gà - (Bại liệt)</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>
                  Vắc-xin phối hợp ngừa các bệnh: Bạch hầu, Uốn ván, Ho gà, (Bại
                  liệt)
                </span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Viêm màng não do Não mô cầu ACWY</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Não mô cầu A,C,Y,W-135</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="3" className="background_ages">
              <div className="ages">
                <span>Từ 9-15 tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Bạch hầu - Uốn ván - Ho gà</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Tdap</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Viêm màng não do Não mô cầu ACWY ở thanh thiếu niên</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Não mô cầu A,C,Y,W-135 (1 mũi)</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Não mô cầu nhóm huyết thanh B</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>
                  Não mô cầu nhóm huyết thanh B (4CMenB: 2 liều cách nhau 1
                  tháng)
                </span>
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_ages">
              <div className="ages">
                <span>Từ 9-26 tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Ung thư cổ tử cung và các bệnh khác do HPV</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Vắc xin HPV</span>
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_ages">
              <div className="ages">
                <span>Từ 26 tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Bạch hầu - Uốn ván - Ho gà</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Tdap (tiêm nhắc mỗi 10 năm)</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="3" className="background_ages">
              <div className="ages">
                <span>Mọi lứa tuổi</span>
              </div>
            </td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Dại</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Vắc xin Dại</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Uốn ván</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Tetanus vắc xin</span>
              </div>
            </td>
          </tr>
          <tr>
            <td rowSpan="0" hidden></td>
            <td className="background_cusCell">
              <div className="can_hits">
                <span>Cúm mùa</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">
                <span>Tiêm hàng năm</span>
              </div>
            </td>
          </tr>
        </table>
      </div>
      <div className="btn_submit">
        <button className="btn">
          <p className="pbtn">Xem lịch tiêm chủng</p>
        </button>
      </div>
    </div>
  );
};

const TableTruongHopDacBiet = () => {
  return (
    <div className="ListTable">
      <div className="tableContent">
        <table>
          <tr>
            <td colSpan="2" className="background_subnautica">
              <div className="subnautica">
                <span>Phụ nữ mang thai</span>
              </div>
            </td>
            <td colSpan={0} hidden></td>
          </tr>
          <tr>
            <td className="background_custom">
              <div className="custom">Bệnh</div>
            </td>
            <td className="background_custom">
              <div className="custom">Vắc-xin</div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Bạch Hầu</span>
              </div>
            </td>
            <td rowSpan={3} className="background_vac-xin">
              <div className="vac-xin">
                Vắc-xin Uốn ván đơn/ Vắc-xin phối hợp ngừa Bạch hầu, Uốn ván, Ho
                gà /Vắc-xin phối hợp ngừa Bạch hầu, Uốn ván
              </div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Uốn ván</span>
              </div>
            </td>
            <td rowSpan={0} hidden></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Ho gà</span>
              </div>
            </td>
            <td rowSpan={0} hidden></td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Bệnh cúm mùa</span>
              </div>
            </td>
            <td className="background_vac-xin">
              <div className="vac-xin">Cúm (IV4/IV3)</div>
            </td>
          </tr>
          <tr>
            <td className="background_sicks">
              <div className="sicks">
                <span>Viêm gan B </span>
              </div>
            </td>
            <td rowSpan={3} className="background_vac-xin">
              <div className="vac-xin">VGB</div>
            </td>
          </tr>
        </table>
      </div>
      <div className="btn_submit">
        <button className="btn">
          <p className="pbtn">Xem lịch tiêm chủng</p>
        </button>
      </div>
    </div>
  );
};

export default Calendar;
