import { useCallback, useEffect, useState } from "react";
import axios, { AxiosRequestConfig } from "axios";
import { API_BASE_URL } from "../config/api";
import { useAdmin } from "../providers/AdminProvider";

export enum Methods {
  get,
  post,
  delete,
}

interface Props {
  method: Methods;
  path: string;
  body?: any;
  withAdminAuth?: boolean;
}

const useFetchData = <T>({ method, path, body, withAdminAuth }: Props) => {
  const [data, setData] = useState<T | undefined>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<unknown | null>(null);

  const { state: admin } = useAdmin();

  const fetchData = useCallback(async () => {
    const config: AxiosRequestConfig = {};
    if (withAdminAuth) {
      config.headers = { Authorization: `Bearer ${admin?.accessToken}` };
    }

    try {
      let response;
      method === Methods.get &&
        ({ data: response } = await axios.get<T>(
          `${API_BASE_URL}${path}`,
          config
        ));
      method === Methods.post &&
        ({ data: response } = await axios.post<T>(
          `${API_BASE_URL}${path}`,
          body,
          config
        ));
      method === Methods.delete &&
        ({ data: response } = await axios.delete<T>(`${API_BASE_URL}${path}`, {
          ...config,
          data: body,
        }));
      setData(response);
    } catch (error) {
      setError(error);
      console.error(error);
    }
    setLoading(false);
  }, [body, method, path, admin, withAdminAuth]);

  useEffect(() => {
    fetchData();
  }, [method, path, body, fetchData]);

  const refetch = () => {
    fetchData();
  };

  return {
    data,
    loading,
    error,
    refetch,
  };
};

export default useFetchData;
