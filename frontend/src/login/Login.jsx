import {useEffect} from 'react';
import {useForm} from "react-hook-form";
import {yupResolver} from '@hookform/resolvers/yup';
import * as Yup from 'yup';
import {useSelector, useDispatch} from 'react-redux';

import {history} from 'shared';
import {authActions} from 'store';

export {Login};

function Login() {
    const dispatch = useDispatch();
    const authUser = useSelector(x => x.auth.user);
    const authError = useSelector(x => x.auth.error);

    useEffect(() => {
        if (authUser) history.navigate('/');
    }, []);

    const validationSchema = Yup.object().shape({
        username: Yup.string().required('Username is required'),
        password: Yup.string().required('Password is required')
    });
    const formOptions = {resolver: yupResolver(validationSchema)};

    const {register, handleSubmit, formState} = useForm(formOptions);
    const {errors} = formState;

    function onSubmit({username, password}) {
        return dispatch(authActions.login({username, password}));
    }

    return (
        <div>
            <div className="col-md-6 offset-md-1 mt-5">
                <div className="card">
                    <h4 className="card-header">Login</h4>
                    <div className="card-body">
                        <form onSubmit={handleSubmit(onSubmit)}>
                            <div className="form-group">
                                <label>Username</label>
                                <input name="username" type="text" {...register('username')}
                                       className={`form-control ${errors.username ? 'is-invalid' : ''}`}/>
                                <div className="invalid-feedback">{errors.username?.message}</div>
                            </div>
                            <div className="form-group">
                                <label>Password</label>
                                <input name="password" type="password" {...register('password')}
                                       className={`form-control ${errors.password ? 'is-invalid' : ''}`}/>
                                <div className="invalid-feedback">{errors.password?.message}</div>
                            </div>
                            <button className="btn btn-primary"> Login</button>
                            {authError &&
                                <div className="alert alert-danger mt-3 mb-0">{authError.message}</div>
                            }
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}
