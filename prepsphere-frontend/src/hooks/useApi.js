import { useState, useEffect, useCallback } from 'react';
import { api } from '../api/client';

export function useApi(endpoint, autoFetch = true) {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(autoFetch);
  const [error, setError] = useState(null);

  const fetchData = useCallback(async (customEndpoint = endpoint) => {
    setLoading(true);
    setError(null);
    try {
      const result = await api.get(customEndpoint);
      setData(result);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [endpoint]);

  useEffect(() => {
    if (autoFetch) {
      fetchData();
    }
  }, [fetchData, autoFetch]);

  const postData = async (path, body) => {
    const res = await api.post(path, body);
    await fetchData();
    return res;
  };

  const patchData = async (path, body) => {
    const res = await api.patch(path, body);
    await fetchData();
    return res;
  };

  const deleteData = async (path) => {
    const res = await api.del(path);
    await fetchData();
    return res;
  };

  return { data, loading, error, fetchData, postData, patchData, deleteData, setData, setError };
}
