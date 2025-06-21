import { DNA } from 'react-loader-spinner';
import './Loader.scss';

const Loader = () => {
  return (
    <div className="loading-overlay">
      <div className="loading-content">
        <DNA
            visible={true}
            height="200"
            width="200"
            ariaLabel="dna-loading"
            wrapperStyle={{}}
            wrapperClass="dna-wrapper"
        />
      </div>
    </div>
  );
};

export default Loader;
