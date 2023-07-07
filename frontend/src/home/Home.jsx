import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { userActions } from 'store';

export { Home };

function Home() {
    const dispatch = useDispatch();
    const { user: authUser } = useSelector(x => x.auth); // todo don't use response from signin, fetch /user principle
    const { users } = useSelector(x => x.users); //todo only call for admin or handle the non auth

    useEffect(() => {
        dispatch(userActions.getAll());
    }, []);

    return (
        <div>
            <h1>User: {authUser?.firstName}!</h1>
            {users.length &&
                <ul>
                    {users.map(user =>
                        <li key={user.id}>{user.firstName} {user.lastName}</li>
                    )}
                </ul>
            }
        </div>
    );
}
