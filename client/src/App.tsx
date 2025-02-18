import { RouterProvider } from 'react-router-dom';
import router from './router';
import { Bounce, ToastContainer } from 'react-toastify';

const App = () => {
  return (
    <>
      <ToastContainer
        position="top-right"
        autoClose={1000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        transition={Bounce}
      />
      <RouterProvider router={router} />
    </>
  );
};

export default App;
